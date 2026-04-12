package com.askmyteacher.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.askmyteacher.app.presentation.ask.AskDoubtScreen
import com.askmyteacher.app.presentation.auth.AuthScreen
import com.askmyteacher.app.presentation.detail.QuestionDetailScreen
import com.askmyteacher.app.presentation.home.HomeScreen
import com.askmyteacher.app.presentation.settings.SettingsScreen
import com.askmyteacher.app.presentation.splash.SplashScreen
import com.askmyteacher.app.presentation.splash.SplashViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AskMyTeacherNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {

        composable<Splash> {

            val viewModel: SplashViewModel = viewModel()

            SplashScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Auth) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        composable<Auth> {

            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Auth> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen(
                onOpenDetail = { questionId ->
                    navController.navigate(Detail(questionId))
                },
                onAskDoubtClick = {
                    navController.navigate(AskDoubt)
                },
                onSettingsClick = {
                    navController.navigate(Settings)
                }
            )
        }

        composable<Detail> { backStackEntry ->

            val detail: Detail = backStackEntry.toRoute()

            QuestionDetailScreen(
                questionId = detail.questionId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<AskDoubt> {

            AskDoubtScreen(
                onSubmit = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Settings> {
            SettingsScreen(
                onLogoutSuccess = {
                    navController.navigate(Auth) {
                        popUpTo<Home> { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}