package com.harissabil.learngue.di

import com.harissabil.learngue.data.datastore.SettingsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {
    single { SettingsManager(androidContext()) }
}