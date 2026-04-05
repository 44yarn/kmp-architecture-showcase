package io.github.mitsuharu.showcase.core.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class PreferenceStorageImpl @Inject constructor(private val dataStore: DataStore<Preferences>,) : PreferenceStorage {

    override suspend fun getString(key: PreferenceKey.StringKey): String? {
        var result: String? = null
        dataStore.edit { prefs -> result = prefs[stringPreferencesKey(key.name)] }
        return result
    }

    override suspend fun putString(key: PreferenceKey.StringKey, value: String) {
        dataStore.edit { prefs -> prefs[stringPreferencesKey(key.name)] = value }
    }

    override suspend fun getBoolean(key: PreferenceKey.BooleanKey): Boolean {
        var result = false
        dataStore.edit { prefs -> result = prefs[booleanPreferencesKey(key.name)] ?: false }
        return result
    }

    override suspend fun putBoolean(key: PreferenceKey.BooleanKey, value: Boolean) {
        dataStore.edit { prefs -> prefs[booleanPreferencesKey(key.name)] = value }
    }

    override fun observeString(key: PreferenceKey.StringKey): Flow<String?> =
        dataStore.data.map { prefs -> prefs[stringPreferencesKey(key.name)] }

    override fun observeBoolean(key: PreferenceKey.BooleanKey): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[booleanPreferencesKey(key.name)] ?: false }

    override suspend fun remove(key: PreferenceKey<*>) {
        dataStore.edit { prefs ->
            when (key) {
                is PreferenceKey.StringKey -> prefs.remove(stringPreferencesKey(key.name))
                is PreferenceKey.BooleanKey -> prefs.remove(booleanPreferencesKey(key.name))
            }
        }
    }

    override suspend fun removeAll() {
        dataStore.edit { it.clear() }
    }
}
