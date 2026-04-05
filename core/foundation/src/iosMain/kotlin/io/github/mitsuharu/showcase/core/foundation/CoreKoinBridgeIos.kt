package io.github.mitsuharu.showcase.core.foundation

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.mp.KoinPlatform

object CoreKoinBridgeIos {
    fun start(vararg modules: Module): KoinApplication =
        startKoin {
            modules(modules.toList())
        }

    fun addModules(vararg modules: Module) {
        KoinPlatform.getKoin().loadModules(modules.toList())
    }

    fun isStarted(): Boolean =
        try {
            KoinPlatform.getKoin()
            true
        } catch (_: Exception) {
            false
        }

    fun stop() {
        stopKoin()
    }
}

fun startKoinForIos(vararg modules: Module): KoinApplication =
    CoreKoinBridgeIos.start(*modules)

fun stopKoinForIos() = CoreKoinBridgeIos.stop()
