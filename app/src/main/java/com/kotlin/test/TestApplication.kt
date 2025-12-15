package com.kotlin.test

import android.app.Application
import com.kotlin.test.feature.serviceModule
import com.kotlin.test.feature.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin once for the entire app lifecycle
        startKoin {
            androidContext(this@TestApplication)
            modules(listOf(viewModule, serviceModule))
        }
    }
}

