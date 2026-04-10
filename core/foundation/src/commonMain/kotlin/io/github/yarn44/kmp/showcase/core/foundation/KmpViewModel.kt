package io.github.yarn44.kmp.showcase.core.foundation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

open class KmpViewModel(externalScope: CoroutineScope? = null,) {
    private val ownJob: Job? = if (externalScope == null) SupervisorJob() else null
    protected val scope: CoroutineScope =
        externalScope ?: CoroutineScope(ownJob!! + Dispatchers.Main.immediate)

    open fun clear() {
        ownJob?.cancel()
    }
}
