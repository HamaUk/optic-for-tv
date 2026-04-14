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
import androidx.compose.runtime.*
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
import com.optic.iptv.ui.components.VideoPlayer

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(DeepCharcoal)) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Brush.radialGradient(listOf(DarkSlateGrey, PureBlack)))
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CategorySidebar(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            Column(modifier = Modifier.weight(1.5f).padding(16.dp)) {
                Text(
                    text = state.selectedCategory?.name ?: "Channels",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val filteredChannels = if (state.selectedCategory != null) {
                    state.channels.filter { it.categoryId == state.selectedCategory!!.id }
                } else state.channels

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredChannels) { channel ->
                        ChannelCard(
                            channel = channel,
                            isSelected = state.selectedChannel?.id == channel.id,
                            onClick = { viewModel.selectChannel(channel) },
                            tvBadge = stringResource(R.string.tv_badge),
                            bitrateLine = stringResource(R.string.hd_bitrate_format, channel.bitrate)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1.5f).padding(16.dp)) {
                PreviewSection(
                    selectedChannel = state.selectedChannel,
                    selectChannelHint = stringResource(R.string.select_channel_preview),
                    programGuideTitle = stringResource(R.string.program_guide),
                    statResolution = stringResource(R.string.stat_resolution),
                    statCodec = stringResource(R.string.stat_codec),
                    statBitrate = stringResource(R.string.stat_bitrate),
                    statFps = stringResource(R.string.stat_fps),
                    epg1Time = stringResource(R.string.epg_item_1_time),
                    epg1Title = stringResource(R.string.epg_item_1_title),
                    epg2Time = stringResource(R.string.epg_item_2_time),
                    epg2Title = stringResource(R.string.epg_item_2_title)
                )
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
        modifier = Modifier.width(260.dp).fillMaxHeight(),
        colors = SurfaceDefaults.colors(containerColor = GlassBackground)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.live_tv),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGold,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = category.id == selectedCategory?.id
                    Surface(
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = if (isSelected) Color.White.copy(alpha = 0.12f) else Color.Transparent,
                            focusedContainerColor = White.copy(alpha = 0.25f),
                            pressedContainerColor = White.copy(alpha = 0.3f)
                        ),
                        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, PrimaryGold.copy(alpha = 0.5f)))
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected) PrimaryGold else White
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(R.string.category_count_format, category.count),
                                style = MaterialTheme.typography.labelLarge,
                                color = White.copy(alpha = 0.4f)
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
            .height(90.dp),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(16.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) White.copy(alpha = 0.1f) else White.copy(alpha = 0.05f),
            focusedContainerColor = White.copy(alpha = 0.2f),
            pressedContainerColor = White.copy(alpha = 0.25f)
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.08f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(3.dp, PrimaryGold))
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp).background(PureBlack, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = tvBadge, color = PrimaryGold.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = White
                )
                Text(
                    text = bitrateLine,
                    style = MaterialTheme.typography.labelLarge,
                    color = White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun PreviewSection(
    selectedChannel: Channel?,
    selectChannelHint: String,
    programGuideTitle: String,
    statResolution: String,
    statCodec: String,
    statBitrate: String,
    statFps: String,
    epg1Time: String,
    epg1Title: String,
    epg2Time: String,
    epg2Title: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16/9f)
                .background(PureBlack, RoundedCornerShape(24.dp))
                .border(2.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedChannel != null) {
                VideoPlayer(
                    url = selectedChannel.url,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(selectChannelHint, color = White.copy(alpha = 0.3f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedChannel != null) {
            Text(
                text = selectedChannel.name,
                style = MaterialTheme.typography.headlineLarge,
                color = White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                StatItem(statResolution, selectedChannel.resolution)
                StatItem(statCodec, selectedChannel.codec)
                StatItem(statBitrate, selectedChannel.bitrate)
                StatItem(statFps, "${selectedChannel.fps}")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(programGuideTitle, style = MaterialTheme.typography.labelLarge, color = PrimaryGold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                EpgItem(epg1Time, epg1Title)
                EpgItem(epg2Time, epg2Title)
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = White.copy(alpha = 0.4f))
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryGold)
    }
}

@Composable
private fun EpgItem(time: String, title: String) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp)).padding(12.dp)) {
        Text(text = time, style = MaterialTheme.typography.bodyLarge, color = White.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = White)
    }
}
