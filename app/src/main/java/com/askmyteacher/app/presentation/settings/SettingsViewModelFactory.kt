package com.askmyteacher.app.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.askmyteacher.app.data.local.AppDatabase

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {

            val database = AppDatabase.getDatabase(context)

            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(database) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}