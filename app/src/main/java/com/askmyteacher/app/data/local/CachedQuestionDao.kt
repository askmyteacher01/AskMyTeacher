package com.askmyteacher.app.data.local

import androidx.room.*
import com.askmyteacher.app.data.model.Question

@Dao
interface CachedQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: CachedQuestionEntity)

    @Query("SELECT * FROM cached_questions WHERE id = :id")
    suspend fun getById(id: String): CachedQuestionEntity?

    @Query("DELETE FROM cached_questions")
    suspend fun clearAll()

    @Query("SELECT * FROM cached_questions WHERE syncStatus = 'PendingSync'")
    suspend fun getPendingSync(): List<CachedQuestionEntity>

    @Query("DELETE FROM cached_questions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM cached_questions ORDER BY createdAt DESC")
    suspend fun getAll(): List<CachedQuestionEntity>

    @Query("DELETE FROM cached_questions WHERE syncStatus = 'Synced'")
    suspend fun deleteSynced()

    @Query("UPDATE cached_questions SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

}

fun CachedQuestionEntity.toQuestion(): Question {
    return Question(
        id = id,
        userId = "",
        questionText = questionText,
        imageUrl = imageUrl,
        answerText = answerText,
        status = status,
        createdAt = createdAt
    )
}
