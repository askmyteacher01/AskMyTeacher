package com.askmyteacher.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.local.AppDatabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val database: AppDatabase
) : ViewModel() {

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            SupabaseManager.client.auth.signOut()
            onLoggedOut()
        }
    }

    fun clearCache(onCleared: () -> Unit) {
        viewModelScope.launch {
            database.cachedQuestionDao().clearAll()
            onCleared()
        }
    }
}