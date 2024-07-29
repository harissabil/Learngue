package com.harissabil.learngue.di

import android.app.Application
import androidx.room.Room
import com.harissabil.learngue.data.local.room.ScannedTextDatabase
import org.koin.dsl.module

fun provideScannedTextDatabase(application: Application): ScannedTextDatabase = Room.databaseBuilder(
    context = application,
    klass = ScannedTextDatabase::class.java,
    name = "scanned_text_db"
)
    .fallbackToDestructiveMigration()
    .build()

fun provideScannedTextDao(database: ScannedTextDatabase) = database.scannedTextDao()

val databaseModule = module {
    single { provideScannedTextDatabase(get()) }
    single { provideScannedTextDao(get()) }
}