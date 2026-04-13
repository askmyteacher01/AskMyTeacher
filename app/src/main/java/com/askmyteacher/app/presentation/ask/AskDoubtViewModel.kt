package com.askmyteacher.app.presentation.ask

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.ai.GeminiService
import com.askmyteacher.app.data.model.Question
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.File
import io.github.jan.supabase.auth.auth
import com.askmyteacher.app.BuildConfig
import com.askmyteacher.app.data.ai.buildGeminiRequest
import com.askmyteacher.app.data.local.AppDatabase
import com.askmyteacher.app.data.local.CachedQuestionEntity
import com.askmyteacher.app.utils.NetworkMonitor
import java.io.FileOutputStream

@Serializable
data class InsertQuestion(
    val user_id: String,
    val question_text: String,
    val image_url: String? = null,
    val status: String = "Pending"
)

class AskDoubtViewModel(
    private val database: AppDatabase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

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
        isOnline: Boolean
    ) {

        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {

            _uiState.update { it.copy(isSubmitting = true, error = null) }

            try {

                if (!isOnline) {

                    val localId = "local_${System.currentTimeMillis()}"

                    val localQuestion = CachedQuestionEntity(
                        id = localId,
                        questionText = state.questionText,
                        imageUrl = null,
                        answerText = null,
                        status = "Pending",
                        createdAt = System.currentTimeMillis().toString(),
                        syncStatus = "PendingSync"
                    )

                    database.cachedQuestionDao().insert(localQuestion)

                    Log.d("OfflineMode", "Saved locally for sync")

                    _uiState.update {
                        it.copy(isSubmitting = false, isSuccess = true)
                    }

                    return@launch
                }


                val user = SupabaseManager.client.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in")

                var imageUrl: String? = null

                if (imageFile != null && state.selectedImageUri != null) {

                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

                    val compressedFile = File(
                        imageFile.parent,
                        "compressed_${System.currentTimeMillis()}.jpg"
                    )

                    val out = FileOutputStream(compressedFile)

                    bitmap.compress(
                        android.graphics.Bitmap.CompressFormat.JPEG,
                        75,
                        out
                    )

                    out.close()

                    val bytes = compressedFile.readBytes()
                    val fileName = "question_${System.currentTimeMillis()}.jpg"

                    val start = System.currentTimeMillis()

                    SupabaseManager.client
                        .storage
                        .from("question-images")
                        .upload(fileName, bytes)

                    val end = System.currentTimeMillis()

                    Log.d("UploadTime", "Upload took ${end - start} ms")

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

                val insertedQuestion = SupabaseManager.client
                    .from("questions")
                    .insert(question) { select() }
                    .decodeSingle<Question>()

                val response = GeminiService.api.generateAnswer(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = buildGeminiRequest(state.questionText)
                )

                val answerText = response.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text

                if (answerText != null && insertedQuestion.id != null) {

                    SupabaseManager.client
                        .from("questions")
                        .update(
                            mapOf(
                                "answer_text" to answerText,
                                "status" to "Answered"
                            )
                        ) {
                            filter { eq("id", insertedQuestion.id) }
                        }
                }

                _uiState.update {
                    it.copy(isSubmitting = false, isSuccess = true)
                }

            } catch (e: Exception) {

                Log.e("SubmitError", "Error", e)

                _uiState.update {
                    it.copy(isSubmitting = false, error = e.message)
                }
            }
        }
    }
}