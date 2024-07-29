package com.harissabil.learngue

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

class MainViewModel : ViewModel() {

    private val _textFromImage = MutableStateFlow<String?>(null)
    val textFromImage: StateFlow<String?> = _textFromImage.asStateFlow()

    fun onScanFromCamera(
        context: Context,
        bitmap: Bitmap,
    ) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage = try {
            InputImage.fromBitmap(bitmap, 0)
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to load image.", Toast.LENGTH_SHORT).show()
            return
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load image.", Toast.LENGTH_SHORT).show()
            return
        }

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Process text recognition result
                val resultText = visionText.text
                _textFromImage.update { resultText }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to recognize text.", Toast.LENGTH_SHORT).show()
                _textFromImage.update { null }
            }
    }

    fun resetTextFromImage() {
        _textFromImage.value = null
    }
}