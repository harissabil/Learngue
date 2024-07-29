package com.harissabil.learngue.data.local

import com.harissabil.learngue.data.local.entity.Answer
import com.harissabil.learngue.data.local.entity.Question
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.data.local.room.ScannedTextDao

class ScannedTextRepository(
    private val scannedTextDao: ScannedTextDao,
) {

    suspend fun insertScannedText(scannedText: ScannedText) =
        scannedTextDao.insertScannedText(scannedText)

    suspend fun deleteScannedText(scannedText: ScannedText) =
        scannedTextDao.deleteScannedText(scannedText)

    suspend fun getScannedTextById(id: Int) = scannedTextDao.getScannedTextById(id)

    fun getAllScannedTexts() = scannedTextDao.getAllScannedTexts()

    suspend fun getScannedTextWithQuestionsAndAnswers() =
        scannedTextDao.getScannedTextWithQuestionsAndAnswers()

    // Question related operations
    suspend fun insertQuestion(question: Question) = scannedTextDao.insertQuestion(question)

    suspend fun deleteQuestionsByNoteId(scannedTextOriginId: Int) =
        scannedTextDao.deleteQuestionsByNoteId(scannedTextOriginId)

    // Answer related operations
    suspend fun insertAllAnswers(answers: List<Answer>) = scannedTextDao.insertAllAnswers(answers)

    suspend fun deleteAnswersByQuestionId(questionId: Int) =
        scannedTextDao.deleteAnswersByQuestionId(questionId)
}