package com.harissabil.learngue.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    foreignKeys = [ForeignKey(
        entity = Question::class,
        parentColumns = ["questionId"],
        childColumns = ["questionOriginId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Answer(
    @PrimaryKey(autoGenerate = true) val answerId: Int = 0,
    val questionOriginId: Int,
    val answerText: String,
    val isCorrect: Boolean,
    val createdAt: Instant = Clock.System.now(),
)
