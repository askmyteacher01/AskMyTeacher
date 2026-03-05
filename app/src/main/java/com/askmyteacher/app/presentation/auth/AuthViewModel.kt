package com.askmyteacher.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChange(email: String) =
        _uiState.update { it.copy(email = email) }

    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password) }

    fun toggleMode() =
        _uiState.update {
            it.copy(
                mode = if (it.mode == AuthMode.LOGIN)
                    AuthMode.REGISTER else AuthMode.LOGIN,
                errorMessage = null
            )
        }

    fun submit(onSuccess: () -> Unit) {

        val state = _uiState.value

        if (!state.canSubmit) return

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            try {

                if (state.mode == AuthMode.LOGIN) {

                    SupabaseManager.client.auth.signInWith(Email) {
                        email = state.email
                        password = state.password
                    }

                } else {

                    SupabaseManager.client.auth.signUpWith(Email) {
                        email = state.email
                        password = state.password
                    }
                }

                _uiState.update { it.copy(isLoading = false) }

                onSuccess()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Authentication failed"
                    )
                }
            }
        }
    }

    fun clearError() =
        _uiState.update { it.copy(errorMessage = null) }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            SupabaseManager.client.auth.signOut()
            onLoggedOut()
        }
    }
}