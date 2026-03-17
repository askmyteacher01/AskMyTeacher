package com.askmyteacher.app.presentation.home

import com.askmyteacher.app.data.model.Question

data class HomeUiState(
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = false,
    val previewQuestion: Question? = null
)