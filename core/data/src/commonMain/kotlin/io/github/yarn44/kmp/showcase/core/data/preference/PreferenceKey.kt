package io.github.yarn44.kmp.showcase.core.data.preference

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * Type-safe preference key definitions.
 * Each typed subclass encapsulates the DataStore key creation for its value type,
 * so callers never need to think about the underlying value type at the call site.
 */
sealed class PreferenceKey<T : Any>(val key: String) {
    abstract fun dataStoreKey(): Preferences.Key<T>

    abstract class StringKey(key: String) : PreferenceKey<String>(key) {
        override fun dataStoreKey(): Preferences.Key<String> = stringPreferencesKey(key)
    }

    abstract class BooleanKey(key: String) : PreferenceKey<Boolean>(key) {
        override fun dataStoreKey(): Preferences.Key<Boolean> = booleanPreferencesKey(key)
    }

    abstract class IntKey(key: String) : PreferenceKey<Int>(key) {
        override fun dataStoreKey(): Preferences.Key<Int> = intPreferencesKey(key)
    }

    abstract class LongKey(key: String) : PreferenceKey<Long>(key) {
        override fun dataStoreKey(): Preferences.Key<Long> = longPreferencesKey(key)
    }

    abstract class FloatKey(key: String) : PreferenceKey<Float>(key) {
        override fun dataStoreKey(): Preferences.Key<Float> = floatPreferencesKey(key)
    }

    abstract class DoubleKey(key: String) : PreferenceKey<Double>(key) {
        override fun dataStoreKey(): Preferences.Key<Double> = doublePreferencesKey(key)
    }

    abstract class StringSetKey(key: String) : PreferenceKey<Set<String>>(key) {
        override fun dataStoreKey(): Preferences.Key<Set<String>> = stringSetPreferencesKey(key)
    }

    /** Authentication-related keys (used by Login and Home flows). */
    object Auth {
        data object SavedEmail : StringKey("saved_email")
        data object RememberEmail : BooleanKey("remember_email")
    }
}
