package showcase.primitive

import showcase.util.configureKmpOrAndroid
import showcase.util.libs
import showcase.util.library
import showcase.util.version

plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

configureKmpOrAndroid(
    androidMain = { project ->
        add("implementation", project.libs.library("daggerHiltCore"))
        add("implementation", project.libs.library("androidxHiltNavCompose"))
    },
    androidOnly = { project ->
        add("ksp", project.libs.library("androidxHiltCompiler"))
        add("implementation", project.libs.library("daggerHiltCore"))
        add("implementation", project.libs.library("androidxHiltNavCompose"))
        add("ksp", project.libs.library("daggerHiltCompiler"))
        add("ksp", project.libs.library("kotlinMetadataJvm"))
    },
    kmpKsp = {
        dependencies.apply {
            add("kspAndroid", libs.library("androidxHiltCompiler"))
            add("kspAndroid", libs.library("daggerHiltCompiler"))
            add("kspAndroid", libs.library("kotlinMetadataJvm"))
        }
    },
)
