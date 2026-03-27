package com.askmyteacher.app.presentation.ask

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.File
import io.github.jan.supabase.auth.auth

@Serializable
data class InsertQuestion(
    val user_id: String,
    val question_text: String,
    val image_url: String? = null,
    val status: String = "Pending"
)

class AskDoubtViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AskDoubtUiState())
    val uiState: StateFlow<AskDoubtUiState> = _uiState

    fun onQuestionChange(text: String) {
        _uiState.update { it.copy(questionText = text) }
    }

    fun setImage(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun submitQuestion(
        imageFile: File?
    ) {

        val state = _uiState.value

        if (!state.canSubmit) return

        viewModelScope.launch {

            _uiState.update { it.copy(isSubmitting = true, error = null) }

            try {

                val user = SupabaseManager.client.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in")

                var imageUrl: String? = null

                if (imageFile != null && state.selectedImageUri != null) {

                    val bytes = imageFile.readBytes()
                    val fileName = "question_${System.currentTimeMillis()}.jpg"

                    SupabaseManager.client
                        .storage
                        .from("question-images")
                        .upload(fileName, bytes)

                    imageUrl = SupabaseManager.client
                        .storage
                        .from("question-images")
                        .publicUrl(fileName)
                }

                val question = InsertQuestion(
                    user_id = user.id,
                    question_text = state.questionText,
                    image_url = imageUrl,
                    status = "Pending"
                )

                SupabaseManager.client
                    .from("questions")
                    .insert(question)

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSuccess = true
                    )
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.message
                    )
                }
            }
        }
    }
}