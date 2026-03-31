package com.askmyteacher.app.data.ai

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)
