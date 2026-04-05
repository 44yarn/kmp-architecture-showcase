package io.github.mitsuharu.showcase.core.data.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    suspend fun getString(key: PreferenceKey.StringKey): String?
    suspend fun putString(key: PreferenceKey.StringKey, value: String)
    suspend fun getBoolean(key: PreferenceKey.BooleanKey): Boolean
    suspend fun putBoolean(key: PreferenceKey.BooleanKey, value: Boolean)
    fun observeString(key: PreferenceKey.StringKey): Flow<String?>
    fun observeBoolean(key: PreferenceKey.BooleanKey): Flow<Boolean>
    suspend fun remove(key: PreferenceKey<*>)
    suspend fun removeAll()
}
