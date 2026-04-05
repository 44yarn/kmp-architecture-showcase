package io.github.mitsuharu.showcase.core.data.preference

sealed class PreferenceKey<T>(val name: String) {
    sealed class StringKey(name: String) : PreferenceKey<String>(name) {
        data object SavedEmail : StringKey("saved_email")
    }

    sealed class BooleanKey(name: String) : PreferenceKey<Boolean>(name) {
        data object RememberEmail : BooleanKey("remember_email")
    }
}
