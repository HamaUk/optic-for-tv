package com.optic.iptv.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.optic.iptv.R
import com.optic.iptv.ui.components.VideoPlayer
import com.optic.iptv.ui.dashboard.DashboardViewModel
import com.optic.iptv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FullscreenPlayerScreen(
    channelId: String,
    dashboardViewModel: DashboardViewModel,
    onBack: () -> Unit
) {
    val state by dashboardViewModel.uiState.collectAsState()
    val channel = state.channels.find { it.id == channelId }
    var showOverlay by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4000)
        showOverlay = false
    }

    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        if (channel != null) {
            VideoPlayer(
                url = channel.url,
                modifier = Modifier.fillMaxSize(),
                showController = true,
                controllerShowTimeoutMs = 4_000
            )

            AnimatedVisibility(
                visible = showOverlay,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xE6000000), Color.Transparent)
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        onClick = onBack,
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Color.White.copy(alpha = 0.12f),
                            focusedContainerColor = PrimaryGold,
                            pressedContainerColor = PrimaryGold.copy(alpha = 0.85f),
                            contentColor = White,
                            focusedContentColor = PureBlack
                        ),
                        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.06f),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, White))
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.player_back),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xCCDD0000), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.player_live),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = White
                            )
                        }
                        Text(
                            text = channel.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = White,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_channel_missing),
                    style = MaterialTheme.typography.headlineLarge,
                    color = White
                )
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    onClick = onBack,
                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = PrimaryGold,
                        focusedContainerColor = PrimaryGold,
                        contentColor = PureBlack
                    )
                ) {
                    Text(
                        text = stringResource(R.string.player_back),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp)
                    )
                }
            }
        }
    }
}
