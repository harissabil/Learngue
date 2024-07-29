package com.harissabil.learngue.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ScannedTextWithQuestionsAndAnswers(
    @Embedded val scannedText: ScannedText,
    @Relation(
        entity = Question::class,
        parentColumn = "scannedTextId",
        entityColumn = "scannedTextOriginId"
    )
    val questions: List<QuestionWithAnswers>,
)
