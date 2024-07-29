package com.harissabil.learngue.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity
data class ScannedText(
    @PrimaryKey(autoGenerate = true) val scannedTextId: Int = 0,
    val originalText: String,
    val language: String,
    val translationText: String,
    val explanation: String,
    val usageExample: String,
    val createdAt: Instant = Clock.System.now(),
)
