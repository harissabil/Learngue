package com.harissabil.learngue.domain

import com.google.gson.Gson
import com.harissabil.learngue.data.local.ScannedTextRepository
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.data.local.entity.ScannedTextWithQuestionsAndAnswers
import kotlinx.datetime.Clock

class GenerateResult(
    private val scannedTextRepository: ScannedTextRepository,
) {

    suspend operator fun invoke(
        jsonString: String,
    ): ScannedText {

        val gson = Gson()
        val scannedTextWithQuestionsAndAnswers =
            gson.fromJson(jsonString, ScannedTextWithQuestionsAndAnswers::class.java)
        val scannedTextId = scannedTextRepository.insertScannedText(
            scannedTextWithQuestionsAndAnswers.scannedText.copy(
                scannedTextId = 0,
                createdAt = Clock.System.now(),
            )
        )

        scannedTextWithQuestionsAndAnswers.questions.forEach { questionWithAnswer ->
            val question = questionWithAnswer.question.copy(
                questionId = 0,
                scannedTextOriginId = scannedTextId.toInt(),
                createdAt = Clock.System.now()
            )
            val questionId = scannedTextRepository.insertQuestion(question)
            val answers = questionWithAnswer.answers.map { answer ->
                answer.copy(
                    answerId = 0,
                    questionOriginId = questionId.toInt(),
                    createdAt = Clock.System.now()
                )
            }
            scannedTextRepository.insertAllAnswers(answers)
        }

        return scannedTextWithQuestionsAndAnswers.scannedText
    }
}