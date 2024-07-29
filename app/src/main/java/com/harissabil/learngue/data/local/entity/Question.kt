package com.harissabil.learngue.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    foreignKeys = [ForeignKey(
        entity = ScannedText::class,
        parentColumns = ["scannedTextId"],
        childColumns = ["scannedTextOriginId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val questionId: Int = 0,
    val scannedTextOriginId: Int,
    val questionText: String,
    val createdAt: Instant? = Clock.System.now(),
)