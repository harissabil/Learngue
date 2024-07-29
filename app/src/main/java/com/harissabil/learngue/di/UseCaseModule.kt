package com.harissabil.learngue.di

import com.harissabil.learngue.domain.GenerateResult
import org.koin.dsl.module

val useCaseModule = module {
     single { GenerateResult(get()) }
}