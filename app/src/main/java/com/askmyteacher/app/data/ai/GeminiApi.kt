package com.askmyteacher.app.data.ai

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {

    @POST("v1beta/models/gemini-3-flash-preview:generateContent")
    suspend fun generateAnswer(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
