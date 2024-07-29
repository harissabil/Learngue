package com.harissabil.learngue.data.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.harissabil.learngue.BuildConfig

class GeminiClient {

    val geneminiFlashModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
        )
    }
}