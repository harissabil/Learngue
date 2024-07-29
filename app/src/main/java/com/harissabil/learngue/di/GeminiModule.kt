package com.harissabil.learngue.di

import com.harissabil.learngue.data.gemini.GeminiClient
import org.koin.dsl.module

val geminiModule = module {
    single { GeminiClient() }
}