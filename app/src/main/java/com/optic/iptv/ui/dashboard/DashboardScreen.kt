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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.itemsIndexed

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardScreen(
    onBackToHub: () -> Unit,
    onPlayChannel: (Channel) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(DeepCharcoal)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DashboardBackgroundBrush)
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
                        .padding(start = 8.dp, end = 28.dp, top = 20.dp, bottom = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                onClick = onBackToHub,
                                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(14.dp)),
                                colors = ClickableSurfaceDefaults.colors(
                                    containerColor = Color.White.copy(alpha = 0.08f),
                                    focusedContainerColor = PrimaryGold,
                                    contentColor = White,
                                    focusedContentColor = PureBlack
                                ),
                                scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
                                border = ClickableSurfaceDefaults.border(
                                    focusedBorder = Border(BorderStroke(2.dp, White.copy(alpha = 0.85f)))
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.nav_back_home),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                                )
                            }
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
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    val filteredChannels = if (state.selectedCategory != null) {
                        state.channels.filter { it.categoryId == state.selectedCategory!!.id }
                    } else {
                        state.channels
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 280.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredChannels, key = { it.id }) { channel ->
                            ChannelCard(
                                channel = channel,
                                isSelected = state.selectedChannel?.id == channel.id,
                                onClick = {
                                    viewModel.selectChannel(channel)
                                    onPlayChannel(channel)
                                },
                                tvBadge = stringResource(R.string.tv_badge)
                            )
                        }
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
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryGold.copy(alpha = 0.85f)
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
    val sidebarShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(categories) {
        if (categories.isNotEmpty()) {
            try { focusRequester.requestFocus() } catch (_: Exception) {}
        }
    }

    Box(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = SidebarBackgroundBrush,
                    shape = sidebarShape
                )
                .border(
                    width = 1.dp,
                    brush = SidebarBorderBrush,
                    shape = sidebarShape
                )
        )
        Column(modifier = Modifier.padding(start = 20.dp, end = 18.dp, top = 24.dp, bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.sidebar_categories),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGold,
                    letterSpacing = 3.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.live_tv),
                style = MaterialTheme.typography.bodyLarge,
                color = White.copy(alpha = 0.45f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(categories) { index, category ->
                    val isSelected = category.id == selectedCategory?.id
                    Surface(
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .let { if (index == 0) it.focusRequester(focusRequester) else it },
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(16.dp)),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = if (isSelected) Color(0x331E2F4A) else Color.Transparent,
                            focusedContainerColor = Color.White.copy(alpha = 0.16f),
                            pressedContainerColor = Color.White.copy(alpha = 0.22f)
                        ),
                        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.03f),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, PrimaryGold.copy(alpha = 0.75f)))
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (isSelected) PrimaryGold else Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) PrimaryGold else White,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
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
    tvBadge: String
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(20.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) Color(0x4026C6DA) else Color.White.copy(alpha = 0.06f),
            focusedContainerColor = Color.White.copy(alpha = 0.16f),
            pressedContainerColor = Color.White.copy(alpha = 0.22f)
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.03f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(3.dp, PrimaryGold))
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(ChannelCardIconBrush, RoundedCornerShape(14.dp))
                    .border(1.dp, PrimaryGold.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (channel.logo.isNotBlank()) {
                    AsyncImage(
                        model = channel.logo,
                        contentDescription = channel.name,
                        modifier = Modifier.size(44.dp)
                    )
                } else {
                    Text(text = tvBadge, color = PrimaryGold.copy(alpha = 0.75f))
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = channel.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = White,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

}
