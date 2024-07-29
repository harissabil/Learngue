package com.harissabil.learngue.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.learngue.data.datastore.SettingsManager
import com.harissabil.learngue.data.local.ScannedTextRepository
import com.harissabil.learngue.data.local.entity.ScannedText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val settingsManager: SettingsManager,
    private val scannedTextRepository: ScannedTextRepository
) : ViewModel() {

    private val _scannedTexts = MutableStateFlow<List<ScannedText>?>(null)
    val scannedTexts: StateFlow<List<ScannedText>?> = _scannedTexts.asStateFlow()

    private val _language = MutableStateFlow(Languages.INDONESIAN)
    val language: StateFlow<Languages> = _language.asStateFlow()

    init {
        getLanguage()
        getScannedTexts()
    }

    private fun getScannedTexts() = viewModelScope.launch {
        scannedTextRepository.getAllScannedTexts().collect {
            _scannedTexts.value = it
        }
    }

    private fun getLanguage() {
        settingsManager.getLanguage().onEach { language ->
            _language.value = language
        }.launchIn(viewModelScope)
    }

    fun setLanguage(language: Languages) = viewModelScope.launch {
        settingsManager.setLanguage(language)
    }
}