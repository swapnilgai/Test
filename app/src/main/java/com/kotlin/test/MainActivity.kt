package com.kotlin.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kotlin.test.util.navigation.AppNavigation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainActivity)
            modules(emptyList()) // Add your modules here when you create them
        }

        setContent {
            AppNavigation()
        }
    }
}

