package com.askmyteacher.app.presentation.splash

import androidx.compose.runtime.*

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAuth: () -> Unit
) {

    val destination by viewModel.destination.collectAsState()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Home -> onNavigateToHome()
            SplashDestination.Auth -> onNavigateToAuth()
            null -> Unit
        }
    }

    SplashContent()
}
