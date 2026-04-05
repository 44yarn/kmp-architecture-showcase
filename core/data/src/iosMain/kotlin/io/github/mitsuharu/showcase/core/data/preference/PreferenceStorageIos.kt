package io.github.mitsuharu.showcase.core.data.preference

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import platform.Foundation.NSUserDefaults

class PreferenceStorageIos : PreferenceStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    // 変更通知用の内部カウンタ（observe 用）
    private val updateSignal = MutableStateFlow(0)

    override suspend fun getString(key: PreferenceKey.StringKey): String? =
        defaults.stringForKey(key.name)

    override suspend fun putString(key: PreferenceKey.StringKey, value: String) {
        defaults.setObject(value, forKey = key.name)
        updateSignal.value++
    }

    override suspend fun getBoolean(key: PreferenceKey.BooleanKey): Boolean =
        defaults.boolForKey(key.name)

    override suspend fun putBoolean(key: PreferenceKey.BooleanKey, value: Boolean) {
        defaults.setBool(value, forKey = key.name)
        updateSignal.value++
    }

    override fun observeString(key: PreferenceKey.StringKey): Flow<String?> =
        updateSignal.map { defaults.stringForKey(key.name) }

    override fun observeBoolean(key: PreferenceKey.BooleanKey): Flow<Boolean> =
        updateSignal.map { defaults.boolForKey(key.name) }

    override suspend fun remove(key: PreferenceKey<*>) {
        defaults.removeObjectForKey(key.name)
        updateSignal.value++
    }

    override suspend fun removeAll() {
        val keys = listOf(
            PreferenceKey.StringKey.SavedEmail.name,
            PreferenceKey.BooleanKey.RememberEmail.name,
        )
        keys.forEach { defaults.removeObjectForKey(it) }
        updateSignal.value++
    }
}
