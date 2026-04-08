package com.askmyteacher.app.data.local

import androidx.room.*

@Dao
interface CachedQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: CachedQuestionEntity)

    @Query("SELECT * FROM cached_questions WHERE id = :id")
    suspend fun getById(id: String): CachedQuestionEntity?

    @Query("DELETE FROM cached_questions")
    suspend fun clearAll()
}