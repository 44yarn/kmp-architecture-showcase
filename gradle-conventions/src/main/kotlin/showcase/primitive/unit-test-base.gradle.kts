package showcase.primitive

import showcase.util.libs
import showcase.util.library

dependencies {
    add("testImplementation", libs.library("junit"))
    add("testImplementation", libs.library("kotestAssertionsCore"))
    add("testImplementation", libs.library("mockk"))
    add("testImplementation", libs.library("truth"))
    add("testImplementation", libs.library("turbine"))
    add("testImplementation", libs.library("kotlinxCoroutinesTest"))
}
