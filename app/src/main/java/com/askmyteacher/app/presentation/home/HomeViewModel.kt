package com.askmyteacher.app.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.local.AppDatabase
import com.askmyteacher.app.data.local.CachedQuestionEntity
import com.askmyteacher.app.data.local.toQuestion
import com.askmyteacher.app.data.model.Question
import com.askmyteacher.app.utils.NetworkMonitor
import com.askmyteacher.app.utils.SyncManager
import com.askmyteacher.app.utils.isNetworkAvailable
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val networkMonitor = NetworkMonitor(context)

    private val database = AppDatabase.getDatabase(context)

    init {
        observeNetwork()
    }

    fun fetchQuestions() {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            try {

                val local = database.cachedQuestionDao()
                    .getAll()
                    .map { it.toQuestion() }

                _uiState.update {
                    it.copy(
                        questions = local,
                        isLoading = false
                    )
                }

                if (isNetworkAvailable(context)) {

                    val syncManager = SyncManager(database)
                    syncManager.syncPending()

                    val user = SupabaseManager.client.auth.currentUserOrNull()
                        ?: return@launch

                    val remote = SupabaseManager.client
                        .from("questions")
                        .select()
                        .decodeList<Question>()
                        .filter { it.userId == user.id }

                    database.cachedQuestionDao().clearAll()

                    remote.forEach {
                        database.cachedQuestionDao().insert(
                            CachedQuestionEntity(
                                id = it.id ?: "",
                                questionText = it.questionText,
                                imageUrl = it.imageUrl,
                                answerText = it.answerText,
                                status = it.status,
                                createdAt = it.createdAt
                            )
                        )
                    }

                    _uiState.update {
                        it.copy(questions = remote)
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    private fun observeNetwork() {

        viewModelScope.launch {

            networkMonitor.isConnected.collect { connected ->
                if (connected) {
                    fetchQuestions()
                }
            }
        }
    }

    fun refresh() = fetchQuestions()
    fun openPreview(question: Question) {
        _uiState.update { it.copy(previewQuestion = question) }
    }
    fun closePreview() {
        _uiState.update { it.copy(previewQuestion = null) }
    }
}
