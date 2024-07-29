package com.harissabil.learngue.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harissabil.learngue.data.local.entity.Answer
import com.harissabil.learngue.data.local.entity.Question
import com.harissabil.learngue.data.local.entity.ScannedText

@Database(
    entities = [ScannedText::class, Question::class, Answer::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ScannedTextDatabase : RoomDatabase() {
    abstract fun scannedTextDao(): ScannedTextDao
}