package com.harissabil.learngue.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswers(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "questionId",
        entityColumn = "questionOriginId"
    )
    val answers: List<Answer>,
)