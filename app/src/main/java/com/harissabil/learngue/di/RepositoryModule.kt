package com.harissabil.learngue.di

import com.harissabil.learngue.data.local.ScannedTextRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { ScannedTextRepository(get()) }
}