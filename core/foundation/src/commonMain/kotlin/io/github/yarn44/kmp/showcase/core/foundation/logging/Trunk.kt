package io.github.yarn44.kmp.showcase.core.foundation.logging

object Trunk {
    interface Logger {
        fun d(tag: String?, message: String)
        fun i(tag: String?, message: String)
        fun w(tag: String?, message: String, throwable: Throwable?)
        fun e(tag: String?, message: String, throwable: Throwable?)
    }

    private val loggers = mutableListOf<Logger>()

    fun plant(logger: Logger) {
        loggers.add(logger)
    }

    fun d(message: String, tag: String? = null) {
        loggers.forEach { it.d(tag, message) }
    }

    fun i(message: String, tag: String? = null) {
        loggers.forEach { it.i(tag, message) }
    }

    fun w(throwable: Throwable? = null, message: String, tag: String? = null) {
        loggers.forEach { it.w(tag, message, throwable) }
    }

    fun e(throwable: Throwable? = null, message: String, tag: String? = null) {
        loggers.forEach { it.e(tag, message, throwable) }
    }
}
