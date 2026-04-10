package io.github.yarn44.kmp.showcase.core.foundation

interface ActivityLauncher {
    sealed interface Target {
        data object Info : Target
    }

    fun launch(target: Target)
}
