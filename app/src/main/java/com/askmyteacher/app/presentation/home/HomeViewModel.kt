package com.askmyteacher.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.model.Question
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

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchQuestions()
    }

    fun fetchQuestions() {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {

                val user = SupabaseManager.client.auth.currentUserOrNull()

                if (user == null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "User not logged in")
                    }
                    return@launch
                }

                val questions = SupabaseManager.client
                    .from("questions")
                    .select()
                    .decodeList<Question>()

                val userQuestions = questions
                    .filter { it.userId == user.id }
                    .sortedByDescending { it.createdAt }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        questions = userQuestions
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun refresh() {
        fetchQuestions()
    }

    fun openPreview(question: Question) {
        _uiState.update { it.copy(previewQuestion = question) }
    }

    fun closePreview() {
        _uiState.update { it.copy(previewQuestion = null) }
    }
}
