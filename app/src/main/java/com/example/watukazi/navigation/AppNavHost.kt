package com.watukazi.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.watukazi.auth.AuthViewModel
import com.example.watukazi.navigation.*
import com.example.watukazi.ui.theme.screens.dashboard.DashboardScreen
import com.example.watukazi.ui.theme.screens.login.LoginScreen
import com.example.watukazi.ui.theme.screens.register.SignUpScreen
import com.example.watukazi.ui.theme.screens.splashscreen.SplashScreen
import com.example.watukazi.ui.theme.screens.workers.UpdateWorkerScreen
import com.example.watukazi.ui.theme.screens.workers.ViewWorkers
import com.watukazi.app.workers.AddWorkerScreen

@Composable
fun <T> AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_SPLASH
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_REGISTER) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }

        composable(ROUTE_REGISTER) {
            SignUpScreen(navController, authViewModel)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(navController, authViewModel)
        }

        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(ROUTE_ADD_WORKER) {
            AddWorkerScreen(navController)
        }

        composable(ROUTE_UPDATE_WORKER) {
            // Replace TODO with actual worker ID logic when ready
            UpdateWorkerScreen(navController, workerId = "")
        }

        composable(ROUTE_VIEW_WORKERS) {
            ViewWorkers(navController)
        }

        composable(ROUTE_WORKER_SELECTION) {
            WorkerSelectionScreen(navController)
        }
    }

    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            navController.navigate(ROUTE_DASHBOARD) {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun WorkerSelectionScreen(navController: NavHostController) {
    // Implement UI here
}
