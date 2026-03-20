package com.askmyteacher.app.presentation.ask

data class AskDoubtUiState(
    val questionText: String = "",
    val selectedImageUri: String? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null
) {
    val canSubmit: Boolean
        get() = questionText.isNotBlank() && !isSubmitting
}