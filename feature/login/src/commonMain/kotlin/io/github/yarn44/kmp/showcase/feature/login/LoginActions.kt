package io.github.yarn44.kmp.showcase.feature.login

data class LoginActions(
    val onEmailChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val onTogglePasswordVisibility: () -> Unit = {},
    val onLogin: () -> Unit = {},
    val onRandomEmail: () -> Unit = {},
    val onLoginFailureDemo: () -> Unit = {},
    val onCancel: () -> Unit = {},
    val onInfo: () -> Unit = {},
)
