#!/usr/bin/env bash

set -Eeuo pipefail

# Sync Compose Multiplatform Resources into the iOS app bundle.
#
# Why this script exists:
#   Compose Multiplatform's Gradle plugin compiles composeResources into .cvr
#   files under each module's build directory, but when the iOS side of a
#   KMP project is written in SwiftUI (i.e. NOT Compose Multiplatform UI),
#   the plugin does not copy those files into the iOS app bundle. The
#   Compose Resources runtime then fails to resolve any StringResource with
#   a MissingResourceException whose path points at
#     <app>/compose-resources/composeResources/<package>/values/<file>.cvr
#
#   This script runs as an Xcode build phase (see iosApp/project.yml),
#   invokes the `assemble<Target>MainResources` Gradle task for each
#   module that ships composeResources, and rsyncs the resulting tree
#   into the built .app bundle at the layout expected at runtime.
#
# When to touch this script:
#   - Add a new module to the MODULES list below when it gains its own
#     src/commonMain/composeResources/values/strings.xml.
#   - Update the platform -> Kotlin/Native target mapping if a new
#     iOS target is introduced.

log() { echo "sync-compose-resources: $*"; }

# Required Xcode environment variables.
: "${PLATFORM_NAME:?Not running under Xcode (PLATFORM_NAME unset)}"
: "${ARCHS:?Not running under Xcode (ARCHS unset)}"
: "${BUILT_PRODUCTS_DIR:?Not running under Xcode (BUILT_PRODUCTS_DIR unset)}"
: "${UNLOCALIZED_RESOURCES_FOLDER_PATH:?Not running under Xcode (UNLOCALIZED_RESOURCES_FOLDER_PATH unset)}"
: "${SRCROOT:?Not running under Xcode (SRCROOT unset)}"

# Xcode build phase scripts do not inherit JAVA_HOME from the user's shell.
# Without this, gradlew falls back to the system default JDK (e.g. JDK 25)
# which the Kotlin Gradle Plugin rejects with a cryptic version-only error.
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Map Xcode platform/arch to the Kotlin/Native target name.
case "${PLATFORM_NAME}" in
    iphoneos)
        KN_TARGET_CAP="IosArm64"
        ;;
    iphonesimulator)
        if [[ "${ARCHS}" == *"arm64"* ]]; then
            KN_TARGET_CAP="IosSimulatorArm64"
        else
            KN_TARGET_CAP="IosX64"
        fi
        ;;
    *)
        log "unsupported platform: ${PLATFORM_NAME}"
        exit 1
        ;;
esac

# Directory name uses the first letter in lowercase
# (e.g. "IosSimulatorArm64" -> "iosSimulatorArm64Main").
KN_TARGET_LC="$(echo "${KN_TARGET_CAP:0:1}" | tr '[:upper:]' '[:lower:]')${KN_TARGET_CAP:1}"

# Locate the repository root (one level up from iosApp/).
PROJECT_ROOT="${SRCROOT}/.."

# Modules that ship their own composeResources/values/strings.xml.
# Add new entries here (Gradle project paths) when a new module gains
# its own composeResources tree.
MODULES=(
    "feature:login"
)

# Run Gradle to (re)assemble the resources for this target.
GRADLE_TASKS=()
for MODULE in "${MODULES[@]}"; do
    GRADLE_TASKS+=(":${MODULE}:assemble${KN_TARGET_CAP}MainResources")
done

log "running Gradle for target ${KN_TARGET_CAP}: ${GRADLE_TASKS[*]}"
(cd "${PROJECT_ROOT}" && ./gradlew "${GRADLE_TASKS[@]}")

# Destination layout expected by the Compose Resources runtime at load time:
#   <app>/compose-resources/composeResources/<package>/<qualifier>/<file>.cvr
DEST_ROOT="${BUILT_PRODUCTS_DIR}/${UNLOCALIZED_RESOURCES_FOLDER_PATH}/compose-resources"
rm -rf "${DEST_ROOT}"
mkdir -p "${DEST_ROOT}/composeResources"

for MODULE in "${MODULES[@]}"; do
    MODULE_PATH="${MODULE//://}"
    SRC="${PROJECT_ROOT}/${MODULE_PATH}/build/generated/compose/resourceGenerator/assembledResources/${KN_TARGET_LC}Main/composeResources"
    if [[ -d "${SRC}" ]]; then
        log "copying ${SRC}/ -> ${DEST_ROOT}/composeResources/"
        rsync -a "${SRC}/" "${DEST_ROOT}/composeResources/"
    else
        log "warning: no resources at ${SRC} (module ${MODULE})"
    fi
done

log "done"
