package com.harissabil.learngue.ui.screen.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.ui.theme.spacing

@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    scannedTexts: List<ScannedText>,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        state = lazyListState,
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(
            items = scannedTexts,
            key = { _, item -> item.scannedTextId }
        ) { _, scannedText ->
            HistoryItem(
                scannedText = scannedText,
                modifier = Modifier.animateItem(),
                onClick = onItemClicked
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}