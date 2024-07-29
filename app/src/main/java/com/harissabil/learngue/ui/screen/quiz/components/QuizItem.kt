package com.harissabil.learngue.ui.screen.quiz.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.learngue.data.local.entity.Answer
import com.harissabil.learngue.data.local.entity.Question
import com.harissabil.learngue.data.local.entity.QuestionWithAnswers
import com.harissabil.learngue.ui.theme.LearngueTheme
import com.harissabil.learngue.ui.theme.spacing

@Composable
fun QuizItem(
    modifier: Modifier = Modifier,
    questionWithAnswers: QuestionWithAnswers,
    selectedAnswer: Int,
    onAnswerSelected: (Int) -> Unit,
    isSubmitted: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(size = 6.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
            ),
        ) {
            Text(
                text = questionWithAnswers.question.questionText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            )
        }

        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

        questionWithAnswers.answers.forEachIndexed { index, answer ->
            Card(
                shape = RoundedCornerShape(size = 6.dp),
                border = if (selectedAnswer == index) {
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
                } else null,
                colors = CardColors(
                    containerColor = if (isSubmitted) {
                        if (answer.isCorrect) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    } else {
                        if (selectedAnswer == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (isSubmitted) {
                        if (answer.isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        if (selectedAnswer == index) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    disabledContainerColor = if (answer.isCorrect) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = if (answer.isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = { onAnswerSelected(index) },
                enabled = !isSubmitted
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Text(
                        text = answer.answerText,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = selectedAnswer == index,
                        enabled = !isSubmitted,
                        onClick = { onAnswerSelected(index) },
                        colors = RadioButtonDefaults.colors().copy(
                            selectedColor = if (!isSubmitted) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                if (answer.isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                                else MaterialTheme.colorScheme.onErrorContainer
                            },
                            unselectedColor = if (!isSubmitted) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                if (answer.isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                                else MaterialTheme.colorScheme.onErrorContainer
                            },
                            disabledSelectedColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                alpha = 0.38f
                            ),
                            disabledUnselectedColor = MaterialTheme.colorScheme.onErrorContainer.copy(
                                alpha = 0.38f
                            )
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun QuizItemPreview() {
    LearngueTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var selectedAnswer by remember { mutableIntStateOf(0) }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                QuizItem(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    selectedAnswer = selectedAnswer,
                    isSubmitted = true,
                    onAnswerSelected = { selectedAnswer = it },
                    questionWithAnswers = QuestionWithAnswers(
                        question = Question(
                            questionId = 1,
                            questionText = "What is the capital of Indonesia?",
                            scannedTextOriginId = 1
                        ),
                        answers = listOf(
                            Answer(
                                answerId = 1,
                                answerText = "Lorem Ipsum Dolor Sit Ut Labore Et Dolore Magna Aliqua Ut Enim Ad Minim Veniam Quis Nostrud Exercitation Ullamco Laboris Nisi Ut Aliquip Ex Ea Commodo Consequat Duis Aute Irure Dolor In Reprehenderit In Voluptate Velit Esse Cillum Dolore Eu Fugiat Nulla Pariatur Excepteur Sint Occaecat Cupidatat Non Proident Sunt In Culpa Qui Officia Deserunt Mollit Anim Id Est Laborum",
                                questionOriginId = 1,
                                isCorrect = true
                            ),
                           Answer(
                                answerId = 2,
                                answerText = "Bandung",
                                questionOriginId = 1,
                                isCorrect = false
                            ),
                            Answer(
                                answerId = 3,
                                answerText = "Surabaya",
                                questionOriginId = 1,
                                isCorrect = false
                            ),
                            Answer(
                                answerId = 4,
                                answerText = "Bali",
                                questionOriginId = 1,
                                isCorrect = false
                            )
                        )
                    )
                )
            }
        }
    }
}