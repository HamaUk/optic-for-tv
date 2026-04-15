package com.optic.iptv.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.optic.iptv.R
import com.optic.iptv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeHubScreen(
    onLiveTv: () -> Unit,
    onMovies: () -> Unit,
    onSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF1A2744), Color(0xFF0A0E14), DeepCharcoal)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 56.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.hub_welcome),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = PrimaryGold.copy(alpha = 0.9f),
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.hub_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = White.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HubCard(
                    modifier = Modifier.weight(1f).height(320.dp),
                    title = stringResource(R.string.hub_live_title),
                    description = stringResource(R.string.hub_live_desc),
                    gradient = listOf(Color(0xFF2B4A7A), Color(0xFF152238)),
                    accent = PrimaryGold,
                    onClick = onLiveTv
                )
                HubCard(
                    modifier = Modifier.weight(1f).height(320.dp),
                    title = stringResource(R.string.hub_movies_title),
                    description = stringResource(R.string.hub_movies_desc),
                    gradient = listOf(Color(0xFF4A2B5C), Color(0xFF221528)),
                    accent = Color(0xFFE070FF),
                    onClick = onMovies
                )
                HubCard(
                    modifier = Modifier.weight(1f).height(320.dp),
                    title = stringResource(R.string.hub_settings_title),
                    description = stringResource(R.string.hub_settings_desc),
                    gradient = listOf(Color(0xFF2D4A40), Color(0xFF121A18)),
                    accent = Color(0xFF4ECDC4),
                    onClick = onSettings
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun HubCard(
    modifier: Modifier,
    title: String,
    description: String,
    gradient: List<Color>,
    accent: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(28.dp)),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(3.dp, accent))
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.04f),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = Color.White.copy(alpha = 0.08f),
            pressedContainerColor = Color.White.copy(alpha = 0.12f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(28.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(4.dp)
                        .background(accent, RoundedCornerShape(2.dp))
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = White
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = White.copy(alpha = 0.65f),
                    maxLines = 3
                )
            }
        }
    }
}
