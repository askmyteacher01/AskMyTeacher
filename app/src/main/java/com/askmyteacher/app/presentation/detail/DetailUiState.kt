package com.askmyteacher.app.presentation.detail

import com.askmyteacher.app.data.model.Question

data class DetailUiState(
    val question: Question? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)