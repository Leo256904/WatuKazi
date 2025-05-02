
package com.watukazi.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watukazi.app.auth.AuthViewModel
import com.watukazi.app.auth.RegisterScreen
import com.watukazi.app.workers.AddWorkerScreen

@Composable
fun AppNavHost(authViewModel: AuthViewModel = viewModel()) {
    if (authViewModel.isLoggedIn) {
        AddWorkerScreen()
    } else {
        RegisterScreen(authViewModel)
    }
}

