package com.askmyteacher.app.utils

import com.askmyteacher.app.BuildConfig
import com.askmyteacher.app.data.SupabaseManager
import com.askmyteacher.app.data.ai.GeminiService
import com.askmyteacher.app.data.ai.buildGeminiRequest
import com.askmyteacher.app.data.local.AppDatabase
import com.askmyteacher.app.data.model.Question
import com.askmyteacher.app.presentation.ask.InsertQuestion
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class SyncManager(
    private val database: AppDatabase
) {

    suspend fun syncPending() {

        val pending = database.cachedQuestionDao().getPendingSync()

        for (local in pending) {

            try {

                val userId = SupabaseManager.client.auth
                    .currentUserOrNull()?.id ?: continue

                val inserted = SupabaseManager.client
                    .from("questions")
                    .insert(
                        InsertQuestion(
                            user_id = userId,
                            question_text = local.questionText,
                            image_url = null,
                            status = "Pending"
                        )
                    ) {
                        select()
                    }
                    .decodeSingle<Question>()

                val response = GeminiService.api.generateAnswer(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = buildGeminiRequest(local.questionText)
                )

                val answerText = response.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text

                if (answerText != null && inserted.id != null) {

                    SupabaseManager.client
                        .from("questions")
                        .update(
                            mapOf(
                                "answer_text" to answerText,
                                "status" to "Answered"
                            )
                        ) {
                            filter { eq("id", inserted.id) }
                        }
                }

                database.cachedQuestionDao().deleteById(local.id)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}