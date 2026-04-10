package com.askmyteacher.app.presentation.ask

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.askmyteacher.app.data.local.AppDatabase
import com.askmyteacher.app.utils.NetworkMonitor

class AskDoubtViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AskDoubtViewModel::class.java)) {

            val database = AppDatabase.getDatabase(context)
            val networkMonitor = NetworkMonitor(context)

            @Suppress("UNCHECKED_CAST")
            return AskDoubtViewModel(
                database = database,
                networkMonitor = networkMonitor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
