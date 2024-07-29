package com.harissabil.learngue.ui.screen.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.learngue.ui.screen.quiz.components.QuizContent
import com.harissabil.learngue.ui.screen.quiz.components.QuizProgressIndicator
import com.harissabil.learngue.ui.screen.quiz.components.QuizTopAppBar
import com.harissabil.learngue.ui.theme.spacing
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = koinViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = {
            state.quizList.size
        }
    )

    var showResult by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.getQuizList()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        topBar = {
            QuizTopAppBar(
                title = state.quizTitle,
                onNavigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            QuizProgressIndicator(
                current = "${pagerState.currentPage + 1}/${state.quizList.size}",
                progress = {
                    if (state.quizList.isNotEmpty()) {
                        (pagerState.currentPage + 1).toFloat() / state.quizList.size.toFloat()
                    } else {
                        0f
                    }
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            QuizContent(
                pagerState = pagerState,
                quizList = state.quizList,
                selectedAnswer = state.selectedAnswers.getOrNull(pagerState.currentPage) ?: -1,
                onAnswerSelected = { viewModel.onAnswerSelected(pagerState.currentPage, it) },
                isSubmitted = state.isSubmitted,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (state.isSubmitted) {
                        if (pagerState.currentPage == state.quizList.size - 1) {
                            showResult = true
                        } else {
                            viewModel.onNext()
                            Timber.d("correctAnswers: ${state.correctAnswers}")
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    } else {
                        viewModel.onSubmit(pagerState.currentPage)
                    }
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (state.isSubmitted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    contentColor = if (state.isSubmitted) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (state.isSubmitted) {
                        if (pagerState.currentPage == state.quizList.size - 1) "View Result" else "Next"
                    } else {
                        "Submit"
                    }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        if (showResult) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = MaterialTheme.spacing.large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Result",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Text(
                        text = "Your score is ${state.correctAnswers} out of ${state.quizList.size}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Button(onClick = onNavigateUp, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Finish")
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    OutlinedButton(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                        viewModel.retry()
                        showResult = false
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}