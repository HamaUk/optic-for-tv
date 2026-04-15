package com.optic.iptv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController
import com.optic.iptv.data.auth.AuthPreferences
import com.optic.iptv.ui.NavGraph
import com.optic.iptv.ui.Screen
import com.optic.iptv.ui.theme.OpticTVProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startRoute = if (AuthPreferences.isLoggedIn(this)) {
            Screen.Home.route
        } else {
            Screen.Login.route
        }
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                OpticTVProTheme {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        startDestination = startRoute
                    )
                }
            }
        }
    }
}
