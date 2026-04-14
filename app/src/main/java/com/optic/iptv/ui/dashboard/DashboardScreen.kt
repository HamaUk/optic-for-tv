package com.optic.iptv.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import com.optic.iptv.R
import com.optic.iptv.data.model.Category
import com.optic.iptv.data.model.Channel
import com.optic.iptv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardScreen(
    onPlayChannel: (Channel) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(DeepCharcoal)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF1E2836), PureBlack, DeepCharcoal)
                    )
                )
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CategorySidebar(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = state.selectedCategory?.name
                                    ?: stringResource(R.string.channels_header),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = White
                            )
                            Text(
                                text = stringResource(
                                    R.string.channels_count_format,
                                    state.channels.size
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = White.copy(alpha = 0.45f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val filteredChannels = if (state.selectedCategory != null) {
                        state.channels.filter { it.categoryId == state.selectedCategory!!.id }
                    } else {
                        state.channels
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredChannels, key = { it.id }) { channel ->
                            ChannelCard(
                                channel = channel,
                                isSelected = state.selectedChannel?.id == channel.id,
                                onClick = {
                                    viewModel.selectChannel(channel)
                                    onPlayChannel(channel)
                                },
                                tvBadge = stringResource(R.string.tv_badge),
                                bitrateLine = stringResource(R.string.hd_bitrate_format, channel.bitrate)
                            )
                        }
                    }

                    if (state.selectedChannel != null) {
                        ChannelDetailBar(
                            channel = state.selectedChannel!!,
                            hint = stringResource(R.string.channel_detail_hint),
                            statResolution = stringResource(R.string.stat_resolution),
                            statCodec = stringResource(R.string.stat_codec),
                            statBitrate = stringResource(R.string.stat_bitrate),
                            statFps = stringResource(R.string.stat_fps)
                        )
                    }
                }

                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.55f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.loading_channels),
                                style = MaterialTheme.typography.headlineLarge,
                                color = White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Optic TV Pro",
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryGold.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CategorySidebar(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(272.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(0.dp),
        colors = SurfaceDefaults.colors(containerColor = Color(0xCC0F1419))
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = stringResource(R.string.live_tv),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGold,
                    letterSpacing = 3.sp
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = category.id == selectedCategory?.id
                    Surface(
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(14.dp)),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = if (isSelected) Color.White.copy(alpha = 0.1f) else Color.Transparent,
                            focusedContainerColor = White.copy(alpha = 0.22f),
                            pressedContainerColor = White.copy(alpha = 0.28f)
                        ),
                        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.04f),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, PrimaryGold.copy(alpha = 0.55f)))
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected) PrimaryGold else White,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(R.string.category_count_format, category.count),
                                style = MaterialTheme.typography.labelLarge,
                                color = White.copy(alpha = 0.38f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ChannelCard(
    channel: Channel,
    isSelected: Boolean,
    onClick: () -> Unit,
    tvBadge: String,
    bitrateLine: String
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(18.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) Color(0x33E5C100) else Color.White.copy(alpha = 0.06f),
            focusedContainerColor = Color.White.copy(alpha = 0.18f),
            pressedContainerColor = Color.White.copy(alpha = 0.24f)
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.04f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(3.dp, PrimaryGold))
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(PureBlack, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = tvBadge, color = PrimaryGold.copy(alpha = 0.55f))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = White,
                    maxLines = 2
                )
                Text(
                    text = bitrateLine,
                    style = MaterialTheme.typography.labelLarge,
                    color = White.copy(alpha = 0.42f)
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ChannelDetailBar(
    channel: Channel,
    hint: String,
    statResolution: String,
    statCodec: String,
    statBitrate: String,
    statFps: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = SurfaceDefaults.colors(containerColor = Color(0xAA0F1419)),
        border = Border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = channel.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.labelLarge,
                color = White.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                StatItem(statResolution, channel.resolution)
                StatItem(statCodec, channel.codec)
                StatItem(statBitrate, channel.bitrate)
                StatItem(statFps, "${channel.fps}")
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = White.copy(alpha = 0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = PrimaryGold
        )
    }
}

@Composable
private fun EpgItem(time: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodyLarge,
            color = White.copy(alpha = 0.55f)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = White
        )
    }
}
