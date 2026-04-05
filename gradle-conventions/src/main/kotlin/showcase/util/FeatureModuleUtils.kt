package showcase.util

import org.gradle.api.Project

fun Project.addFeatureModuleDependencies(
    configurationType: String = "implementation",
) {
    val featureProjects = rootProject.subprojects.filter {
        it.path.startsWith(":feature:")
    }
    featureProjects.forEach { featureProject ->
        dependencies.add(configurationType, project(featureProject.path))
    }
}
