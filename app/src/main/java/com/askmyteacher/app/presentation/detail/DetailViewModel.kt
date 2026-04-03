package com.askmyteacher.app.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.model.Question
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun loadQuestion(questionId: String) {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {

                val question = SupabaseManager.client
                    .from("questions")
                    .select {
                        filter {
                            eq("id", questionId)
                        }
                    }
                    .decodeSingle<Question>()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        question = question
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

    fun deleteQuestion(
        questionId: String,
        onDeleted: () -> Unit
    ) {
        viewModelScope.launch {

            try {

                SupabaseManager.client
                    .from("questions")
                    .delete {
                        filter { eq("id", questionId) }
                    }

                onDeleted()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message)
                }
            }
        }
    }
}