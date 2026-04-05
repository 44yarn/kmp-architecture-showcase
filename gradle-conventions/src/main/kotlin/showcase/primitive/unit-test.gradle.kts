package showcase.primitive

plugins {
    id("showcase.primitive.unit-test-base")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
