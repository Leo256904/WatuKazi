
package com.watukazi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.watukazi.ui.theme.WatuKaziTheme
//import com.watukazi.app.ui.theme.WatuKaziTheme
import com.watukazi.app.ui.components.SplashScreen
import com.watukazi.app.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatuKaziTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen { showSplash = false }
                } else {
                    AppNavHost()
                }
            }
        }
    }
}
