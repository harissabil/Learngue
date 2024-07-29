package com.harissabil.learngue.ui.screen.quiz

import com.harissabil.learngue.data.local.entity.QuestionWithAnswers

data class QuizState(
    val quizTitle: String = "",
    val quizList: List<QuestionWithAnswers> = emptyList(),
    val selectedAnswers: List<Int> = emptyList(),
    val correctAnswers: Int = 0,
    val isSubmitted: Boolean = false,
)
