package com.optic.iptv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.optic.iptv.ui.NavGraph
import com.optic.iptv.ui.theme.OpticTVProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpticTVProTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
