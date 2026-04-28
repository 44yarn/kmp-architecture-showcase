package io.github.yarn44.kmp.showcase.core.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Type-safe wrapper around DataStore Preferences.
 * The value type is carried by the [PreferenceKey] itself, so callers do not need
 * to think about it at the call site.
 */
class PreferenceStorage(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun <T : Any> getOrNull(key: PreferenceKey<T>): T? =
        dataStore.data.first()[key.dataStoreKey()]

    suspend fun <T : Any> getOrDefault(key: PreferenceKey<T>, default: T): T =
        getOrNull(key) ?: default

    fun <T : Any> observe(key: PreferenceKey<T>): Flow<T?> =
        dataStore.data
            .map { it[key.dataStoreKey()] }
            .distinctUntilChanged()

    suspend fun <T : Any> put(key: PreferenceKey<T>, value: T) {
        dataStore.edit { it[key.dataStoreKey()] = value }
    }

    suspend fun <T : Any> remove(key: PreferenceKey<T>) {
        dataStore.edit { it.remove(key.dataStoreKey()) }
    }

    suspend fun removeAll() {
        dataStore.edit { it.clear() }
    }
}
