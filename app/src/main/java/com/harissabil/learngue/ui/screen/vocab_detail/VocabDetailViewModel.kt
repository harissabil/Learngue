package com.harissabil.learngue.ui.screen.vocab_detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import com.harissabil.learngue.data.datastore.SettingsManager
import com.harissabil.learngue.data.gemini.GeminiClient
import com.harissabil.learngue.data.local.ScannedTextRepository
import com.harissabil.learngue.data.local.entity.ScannedText
import com.harissabil.learngue.domain.GenerateResult
import com.harissabil.learngue.ui.screen.home.Languages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class VocabDetailViewModel(
    private val geminiClient: GeminiClient,
    private val settingsManager: SettingsManager,
    private val scannedTextRepository: ScannedTextRepository,
    private val generateResult: GenerateResult
) : ViewModel() {

    private val _originLanguage = MutableStateFlow<Languages?>(null)

    private val _scannedTextResult = MutableStateFlow<ScannedText?>(null)
    val scannedTextResult: StateFlow<ScannedText?> = _scannedTextResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        getOriginLanguage()
    }

    private fun getOriginLanguage() = viewModelScope.launch {
        settingsManager.getLanguage().onEach { _originLanguage.update { it } }
    }

    fun getScannedTextResult(context: Context, text: String) {
        if (_isLoading.value) return

        _isLoading.update { true }
        Toast.makeText(context, "Getting result...", Toast.LENGTH_SHORT).show()

        val generativeModel = geminiClient.geneminiFlashModel

        val fromLanguage = _originLanguage.value ?: Languages.INDONESIAN

        val inputContent = content {
            text(
                "Generate a JSON string representation of \"$text\" with its translation to English from $fromLanguage and three questions, each with four possible answers. Ensure the JSON structure correctly represents a list of questions with their answers, where each answer has a field indicating if it is correct. The questions and answers must be in the english, and relevant to the scanned text translation, like what is the definition of it and what is the correct way to use it in the sentence, etc. The JSON should follow the structure outlined below." +
                        "\n\n" +
                        "Here is an example of the JSON structure we want:\n" +
                        "\n" +
                        "{\n" +
                        "  \"scannedText\": {\n" +
                        "    \"scannedTextId\": 1,\n" +
                        "    \"originalText\": \"$text\",\n" +
                        "    \"language\": \"$fromLanguage\",\n" +
                        "    \"translationText\": \"The English translation\",\n" +
                        "    \"explanation\": \"Explanation about the translation\",\n" +
                        "    \"usageExample\": \"Example of the usage of the English translation only in the sentence (do not include the original text in this usage example! it is prohibited!)\",\n" +
                        "    \"createdAt\": null\n" +
                        "  },\n" +
                        "  \"questions\": [\n" +
                        "    {\n" +
                        "      \"question\": {\n" +
                        "        \"questionId\": 1,\n" +
                        "        \"scannedTextOriginId\": 1,\n" +
                        "        \"questionText\": \"Question 1\",\n" +
                        "        \"createdAt\": null\n" +
                        "      },\n" +
                        "      \"answers\": [\n" +
                        "        {\n" +
                        "          \"answerId\": 1,\n" +
                        "          \"questionOriginId\": 1,\n" +
                        "          \"answerText\": \"Answer 1\",\n" +
                        "          \"isCorrect\": true,\n" +
                        "          \"createdAt\": \"2023-07-28T12:34:56Z\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 2,\n" +
                        "          \"questionOriginId\": 1,\n" +
                        "          \"answerText\": \"Answer 2\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 3,\n" +
                        "          \"questionOriginId\": 1,\n" +
                        "          \"answerText\": \"Answer 3\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 4,\n" +
                        "          \"questionOriginId\": 1,\n" +
                        "          \"answerText\": \"Answer 4\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"question\": {\n" +
                        "        \"questionId\": 2,\n" +
                        "        \"scannedTextOriginId\": 1,\n" +
                        "        \"questionText\": \"Question 2\",\n" +
                        "        \"createdAt\": null\n" +
                        "      },\n" +
                        "      \"answers\": [\n" +
                        "        {\n" +
                        "          \"answerId\": 5,\n" +
                        "          \"questionOriginId\": 2,\n" +
                        "          \"answerText\": \"Answer 1\",\n" +
                        "          \"isCorrect\": true,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 6,\n" +
                        "          \"questionOriginId\": 2,\n" +
                        "          \"answerText\": \"Answer 2\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 7,\n" +
                        "          \"questionOriginId\": 2,\n" +
                        "          \"answerText\": \"Answer 3\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 8,\n" +
                        "          \"questionOriginId\": 2,\n" +
                        "          \"answerText\": \"Answer 4\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": bull\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"question\": {\n" +
                        "        \"questionId\": 3,\n" +
                        "        \"scannedTextOriginId\": 1,\n" +
                        "        \"questionText\": \"Question 3\",\n" +
                        "        \"createdAt\": null\n" +
                        "      },\n" +
                        "      \"answers\": [\n" +
                        "        {\n" +
                        "          \"answerId\": 9,\n" +
                        "          \"questionOriginId\": 3,\n" +
                        "          \"answerText\": \"Answer 1\",\n" +
                        "          \"isCorrect\": true,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 10,\n" +
                        "          \"questionOriginId\": 3,\n" +
                        "          \"answerText\": \"Answer 2\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 11,\n" +
                        "          \"questionOriginId\": 3,\n" +
                        "          \"answerText\": \"Answer 3\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"answerId\": 12,\n" +
                        "          \"questionOriginId\": 3,\n" +
                        "          \"answerText\": \"Answer 4\",\n" +
                        "          \"isCorrect\": false,\n" +
                        "          \"createdAt\": null\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n" +
                        "\n"
            )
        }

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(inputContent)
                val lines = response.text.toString().trim().lines()
                val fileteredLines = lines.subList(1, lines.size - 1)
                val result = fileteredLines.joinToString("\n")
                Timber.d(result)

                val scannedText = generateResult(jsonString = result)

                Timber.d(scannedText.toString())

                _scannedTextResult.update { scannedText }
                _isLoading.update { false }
            } catch (e: Exception) {
                Timber.e(e)
                Toast.makeText(context, "Failed to generate result.", Toast.LENGTH_SHORT).show()
                _isLoading.update { false }
            }
        }
    }

    fun getScannedText(scannedTextId: Int) = viewModelScope.launch {
        val scannedText = scannedTextRepository.getScannedTextById(scannedTextId)
        _scannedTextResult.update { scannedText }
    }
}