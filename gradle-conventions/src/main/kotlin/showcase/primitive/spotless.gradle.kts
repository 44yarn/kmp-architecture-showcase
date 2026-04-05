package showcase.primitive

import com.diffplug.gradle.spotless.SpotlessExtension
import showcase.util.libs
import showcase.util.version

plugins {
    id("com.diffplug.spotless")
}

configure<SpotlessExtension> {
    kotlin {
        target("src/**/*.kt")
        ktlint(libs.version("ktlint"))
    }
}
