package com.harissabil.learngue.ui.screen.quiz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harissabil.learngue.data.local.entity.QuestionWithAnswers

@Composable
fun QuizContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    quizList: List<QuestionWithAnswers>,
    selectedAnswer: Int,
    onAnswerSelected: (Int) -> Unit,
    isSubmitted: Boolean,
) {
    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
//        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.large),
//        pageSpacing = MaterialTheme.spacing.medium,
        userScrollEnabled = false,
    ) { index ->
        QuizItem(
            questionWithAnswers = quizList[index],
            selectedAnswer = selectedAnswer,
            onAnswerSelected = onAnswerSelected,
            isSubmitted = isSubmitted,
        )
    }
}