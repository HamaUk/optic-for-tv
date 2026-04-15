package com.optic.iptv.ui.movies

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
fun MoviesScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2D1B3D), PureBlack)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.movies_title),
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = SurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                colors = SurfaceDefaults.colors(containerColor = Color(0x44E070FF))
            ) {
                Text(
                    text = stringResource(R.string.movies_coming_soon),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = White
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.movies_description),
                style = MaterialTheme.typography.bodyLarge,
                color = White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 720.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Surface(
                onClick = onBack,
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(16.dp)),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color.White.copy(alpha = 0.1f),
                    focusedContainerColor = PrimaryGold,
                    contentColor = White,
                    focusedContentColor = PureBlack
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1.06f),
                border = ClickableSurfaceDefaults.border(
                    focusedBorder = Border(BorderStroke(2.dp, White))
                )
            ) {
                Text(
                    text = stringResource(R.string.movies_back),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp)
                )
            }
        }
    }
}
