package com.askmyteacher.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(

    val id: String? = null,

    @SerialName("user_id")
    val userId: String,

    @SerialName("question_text")
    val questionText: String,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("answer_text")
    val answerText: String? = null,

    val status: String = "Pending",

    @SerialName("created_at")
    val createdAt: String? = null
)
