package com.harissabil.learngue.di

import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    operator fun invoke(): Module = module {
        includes(
            listOf(
                databaseModule,
                geminiModule,
                useCaseModule,
                repositoryModule,
                datastoreModule,
                viewModelModule
            )
        )
    }
}