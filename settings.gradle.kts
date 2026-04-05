pluginManagement {
    includeBuild("gradle-conventions")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-architecture-showcase"

include(":app")
include(":shared")
include(":core:foundation")
include(":core:ui-kit")
include(":core:data")
include(":feature:login")
include(":feature:home")
include(":feature:info")
