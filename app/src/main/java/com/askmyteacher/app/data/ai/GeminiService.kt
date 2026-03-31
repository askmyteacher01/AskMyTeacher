package com.askmyteacher.app.data.ai

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeminiService {

    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: GeminiApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeminiApi::class.java)
}

fun buildGeminiRequest(question: String): GeminiRequest {
    return GeminiRequest(
        contents = listOf(
            Content(
                parts = listOf(
                    Part(text = question)
                )
            )
        )
    )
}