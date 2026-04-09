package com.askmyteacher.app.presentation.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.local.AppDatabase
import com.askmyteacher.app.data.local.CachedQuestionEntity
import com.askmyteacher.app.data.model.Question
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    private val dao = AppDatabase
        .getDatabase(context)
        .cachedQuestionDao()

    fun loadQuestion(questionId: String) {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {

                val question = SupabaseManager.client
                    .from("questions")
                    .select {
                        filter { eq("id", questionId) }
                    }
                    .decodeSingle<Question>()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        question = question
                    )
                }

                question.id?.let {
                    dao.insert(
                        CachedQuestionEntity(
                            id = it,
                            questionText = question.questionText,
                            imageUrl = question.imageUrl,
                            answerText = question.answerText,
                            status = question.status,
                            createdAt = question.createdAt
                        )
                    )
                }

            } catch (e: Exception) {

                val cached = dao.getById(questionId)

                if (cached != null) {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            question = Question(
                                id = cached.id,
                                userId = "",
                                questionText = cached.questionText,
                                imageUrl = cached.imageUrl,
                                answerText = cached.answerText,
                                status = cached.status,
                                createdAt = cached.createdAt
                            )
                        )
                    }

                } else {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No internet & no cached data"
                        )
                    }
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

                dao.getById(questionId)?.let {
                    dao.clearAll()
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