package com.harissabil.learngue.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.learngue.data.local.ScannedTextRepository
import com.harissabil.learngue.data.local.entity.ScannedText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val scannedTextRepository: ScannedTextRepository,
) : ViewModel() {

    private val _scannedTexts = MutableStateFlow<List<ScannedText>?>(null)
    val scannedTexts: StateFlow<List<ScannedText>?> = _scannedTexts.asStateFlow()

    init {
        getScannedTexts()
    }

    private fun getScannedTexts() = viewModelScope.launch {
        scannedTextRepository.getAllScannedTexts().collect {
            _scannedTexts.value = it
        }
    }
}