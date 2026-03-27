package com.askmyteacher.app.presentation.ask

import android.net.Uri

data class AskDoubtUiState(
    val questionText: String = "",
    val selectedImageUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) {
    val canSubmit: Boolean
        get() = questionText.isNotBlank() && !isSubmitting
}