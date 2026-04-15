package com.optic.iptv.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.optic.iptv.ui.auth.LoginScreen
import com.optic.iptv.ui.dashboard.DashboardScreen
import com.optic.iptv.ui.dashboard.DashboardViewModel
import com.optic.iptv.ui.home.HomeHubScreen
import com.optic.iptv.ui.movies.MoviesScreen
import com.optic.iptv.ui.player.FullscreenPlayerScreen
import com.optic.iptv.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object LiveTv : Screen("live_tv")
    data object Movies : Screen("movies")
    data object Settings : Screen("settings")
    data object Player : Screen("player/{channelId}") {
        fun createRoute(channelId: String) = "player/${Uri.encode(channelId)}"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeHubScreen(
                onLiveTv = { navController.navigate(Screen.LiveTv.route) },
                onMovies = { navController.navigate(Screen.Movies.route) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.LiveTv.route) {
            DashboardScreen(
                onBackToHub = { navController.popBackStack() },
                onPlayChannel = { channel ->
                    if (channel.id.isNotBlank()) {
                        navController.navigate(Screen.Player.createRoute(channel.id))
                    }
                }
            )
        }
        composable(Screen.Movies.route) {
            MoviesScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            val context = LocalContext.current
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onSignOut = {
                    com.optic.iptv.data.auth.AuthPreferences.setLoggedIn(context, false)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("channelId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("channelId").orEmpty()
            val channelId = Uri.decode(encoded)
            val parentEntry = remember {
                navController.getBackStackEntry(Screen.LiveTv.route)
            }
            val dashboardVm: DashboardViewModel = viewModel(parentEntry)
            FullscreenPlayerScreen(
                channelId = channelId,
                dashboardViewModel = dashboardVm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
