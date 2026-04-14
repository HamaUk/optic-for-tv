package com.optic.iptv.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.optic.iptv.ui.auth.LoginScreen
import com.optic.iptv.ui.dashboard.DashboardScreen
import com.optic.iptv.ui.dashboard.DashboardViewModel
import com.optic.iptv.ui.player.FullscreenPlayerScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard")
    data object Player : Screen("player/{channelId}") {
        fun createRoute(channelId: String) = "player/${Uri.encode(channelId)}"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onPlayChannel = { channel ->
                    if (channel.id.isNotBlank()) {
                        navController.navigate(Screen.Player.createRoute(channel.id))
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
                navController.getBackStackEntry(Screen.Dashboard.route)
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
