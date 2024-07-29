package com.harissabil.learngue.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.harissabil.learngue.data.local.entity.Answer
import com.harissabil.learngue.data.local.entity.Question
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.data.local.entity.ScannedTextWithQuestionsAndAnswers
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedTextDao {

    @Insert
    suspend fun insertScannedText(scannedText: ScannedText): Long

    @Delete
    suspend fun deleteScannedText(scannedText: ScannedText)

    @Query("SELECT * FROM ScannedText WHERE scannedTextId = :id")
    suspend fun getScannedTextById(id: Int): ScannedText

    @Query("SELECT * FROM ScannedText")
    fun getAllScannedTexts(): Flow<List<ScannedText>>

    @Transaction
    @Query("SELECT * FROM ScannedText")
    suspend fun getScannedTextWithQuestionsAndAnswers(): List<ScannedTextWithQuestionsAndAnswers>

    // Question related queries

    // Question related queries
    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Query("DELETE FROM question WHERE scannedTextOriginId = :scannedTextOriginId")
    suspend fun deleteQuestionsByNoteId(scannedTextOriginId: Int)

    // Answer related queries
    @Insert
    suspend fun insertAllAnswers(answers: List<Answer>): List<Long>

    @Query("DELETE FROM answer WHERE questionOriginId = :questionId")
    suspend fun deleteAnswersByQuestionId(questionId: Int)
}