package com.harissabil.learngue.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.harissabil.learngue.ui.screen.home.components.LanguageSelector
import com.harissabil.learngue.ui.theme.spacing
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToQuizScreen: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val scannedTexts by viewModel.scannedTexts.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Learngue",
                    fontWeight = FontWeight.SemiBold
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    vertical = MaterialTheme.spacing.small,
                    horizontal = MaterialTheme.spacing.medium
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select your native language:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            LanguageSelector(language = language, onLanguageSelected = viewModel::setLanguage)
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.large))
            Text(
                text = "After you have scanned the text with your phone, you can start the quiz.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            Button(
                onClick = {
                    if (scannedTexts.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            "Please scan a text first",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    onNavigateToQuizScreen()
                }
            ) {
                Text(text = "Start quiz")
            }
        }
    }
}