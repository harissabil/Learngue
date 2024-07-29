package com.harissabil.learngue

import android.app.Application
import com.harissabil.learngue.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class Learngue : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@Learngue)
            modules(AppModules())
        }
    }
}