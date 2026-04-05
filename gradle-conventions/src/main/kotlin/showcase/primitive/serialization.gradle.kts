package showcase.primitive

import showcase.util.configureKmpOrAndroid
import showcase.util.libs
import showcase.util.library

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

configureKmpOrAndroid(
    commonMain = { project ->
        add("implementation", project.libs.library("kotlinxSerializationJson"))
    },
    androidOnly = { project ->
        add("implementation", project.libs.library("kotlinxSerializationJson"))
    },
)
