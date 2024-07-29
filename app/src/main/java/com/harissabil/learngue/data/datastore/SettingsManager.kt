package com.harissabil.learngue.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.learngue.ui.screen.home.Languages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsManager(
    private val context: Context,
) {
    suspend fun setLanguage(language: Languages) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.LANGUAGE] = language.display
        }
    }

    fun getLanguage(): Flow<Languages> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.LANGUAGE]?.let { languageDisplay ->
                Languages.entries.find { it.display == languageDisplay }
            } ?: Languages.INDONESIAN
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "language")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val LANGUAGE = stringPreferencesKey("language")
}