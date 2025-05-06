package com.watukazi.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.watukazi.auth.AuthViewModel
import com.example.watukazi.navigation.ROUTE_ADD_WORKER
import com.example.watukazi.navigation.ROUTE_DASHBOARD
import com.example.watukazi.navigation.ROUTE_LOGIN
import com.example.watukazi.navigation.ROUTE_REGISTER
import com.example.watukazi.navigation.ROUTE_SPLASH
import com.example.watukazi.navigation.ROUTE_UPDATE_WORKER
import com.example.watukazi.navigation.ROUTE_VIEW_WORKERS
import com.example.watukazi.ui.theme.screens.dashboard.DashboardScreen
import com.example.watukazi.ui.theme.screens.login.LoginScreen
import com.example.watukazi.ui.theme.screens.register.SignUpScreen
import com.example.watukazi.ui.theme.screens.splashscreen.SplashScreen
import com.example.watukazi.ui.theme.screens.workers.UpdateWorkerScreen
import com.example.watukazi.ui.theme.screens.workers.ViewWorkers
import com.watukazi.app.workers.AddWorkerScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_SPLASH
    ) {
//        composable(ROUTE_SPLASH) {
//            SplashScreen(navController)
//        }

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
            UpdateWorkerScreen(
                navController,
                productId = TODO()
            )
        }

        composable(ROUTE_VIEW_WORKERS) {
            ViewWorkers(navController)
        }
    }

    // Auto-navigate if already logged in
    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            navController.navigate(ROUTE_DASHBOARD) {
                popUpTo(0) // Clear backstack
            }
        }
    }
}
