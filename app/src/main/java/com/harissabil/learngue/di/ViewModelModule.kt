package com.harissabil.learngue.di

import com.harissabil.learngue.MainViewModel
import com.harissabil.learngue.ui.screen.history.HistoryViewModel
import com.harissabil.learngue.ui.screen.home.HomeViewModel
import com.harissabil.learngue.ui.screen.quiz.QuizViewModel
import com.harissabil.learngue.ui.screen.vocab_detail.VocabDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::VocabDetailViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::QuizViewModel)
}