package com.askmyteacher.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_questions")
data class CachedQuestionEntity(

    @PrimaryKey
    val id: String,

    val questionText: String,
    val imageUrl: String?,
    val answerText: String?,
    val status: String,
    val createdAt: String?
)