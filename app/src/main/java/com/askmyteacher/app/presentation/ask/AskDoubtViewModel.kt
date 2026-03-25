package com.askmyteacher.app.presentation.ask

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

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
        imageFile: File?,
        onSuccess: (String, String?) -> Unit
    ) {

        val state = _uiState.value

        if (!state.canSubmit) return

        viewModelScope.launch {

            _uiState.update { it.copy(isSubmitting = true, error = null) }

            try {

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

                _uiState.update { it.copy(isSubmitting = false) }

                onSuccess(state.questionText, imageUrl)

            } catch (e: Exception) {

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