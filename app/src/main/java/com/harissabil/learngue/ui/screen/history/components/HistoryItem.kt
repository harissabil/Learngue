package com.harissabil.learngue.ui.screen.history.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.ui.theme.LearngueTheme
import com.harissabil.learngue.ui.theme.spacing
import kotlinx.datetime.Clock

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    scannedText: ScannedText,
    onClick: (Int) -> Unit,
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onClick(scannedText.scannedTextId)
        }) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp
            )
        ) {
            Text(
                text = scannedText.originalText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            )
        }
        OutlinedCard(
            shape = RoundedCornerShape(
                topStart = 0.dp, topEnd = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                Text(
                    text = scannedText.translationText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(top = MaterialTheme.spacing.medium)
                )
                Text(
                    text = scannedText.usageExample,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(bottom = MaterialTheme.spacing.medium)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HistoryItemPreview() {
    LearngueTheme {
        Surface {
            HistoryItem(
                scannedText = ScannedText(
                    originalText = "Hello",
                    translationText = "Hallo",
                    explanation = "A greeting",
                    usageExample = "Hallo, wie geht's?",
                    scannedTextId = 1,
                    createdAt = Clock.System.now(),
                    language = "en"
                ),
                onClick = {}
            )
        }
    }
}