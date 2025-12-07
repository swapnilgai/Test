package com.kotlin.test

import android.app.Application
import com.kotlin.test.feature.counter.counterModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin once for the entire app lifecycle
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TestApplication)
            modules(listOf(counterModule))
        }
    }
}

