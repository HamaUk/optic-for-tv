package com.optic.iptv.ui.theme

import androidx.compose.ui.graphics.Color

val DarkSlateGrey = Color(0xFF1A1F26)
val DeepCharcoal = Color(0xFF0E1217)
val PureBlack = Color(0xFF000000)
val PrimaryGold = Color(0xFFE5C100)
val GlassBackground = Color(0x661C2430)
val FocusedGlow = Color(0xFF00A3FF)
val White = Color(0xFFFFFFFF)
val GreyText = Color(0x99FFFFFF)

val DashboardBackgroundBrush = androidx.compose.ui.graphics.Brush.radialGradient(colors = listOf(Color(0xFF1E2A3A), Color(0xFF0D1118), DeepCharcoal))
val SidebarBackgroundBrush = androidx.compose.ui.graphics.Brush.horizontalGradient(colors = listOf(Color(0xF00F1419), Color(0xE6161F2E)))
val SidebarBorderBrush = androidx.compose.ui.graphics.Brush.verticalGradient(colors = listOf(PrimaryGold.copy(alpha = 0.35f), Color(0x2200A3FF), PrimaryGold.copy(alpha = 0.2f)))
val ChannelCardIconBrush = androidx.compose.ui.graphics.Brush.radialGradient(colors = listOf(Color(0xFF2A3444), PureBlack))
