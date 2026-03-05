package com.askmyteacher.app.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onAuthSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    AuthContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = { viewModel.submit(onAuthSuccess) },
        onToggleMode = viewModel::toggleMode,
        onErrorShown = viewModel::clearError
    )
}