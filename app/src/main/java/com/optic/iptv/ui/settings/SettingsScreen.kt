package com.optic.iptv.ui.settings

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import com.optic.iptv.BuildConfig
import com.optic.iptv.R
import com.optic.iptv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

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
                        listOf(Color(0xFF1A2520), PureBlack)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Surface(
                    onClick = onBack,
                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(14.dp)),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = Color.White.copy(alpha = 0.08f),
                        focusedContainerColor = PrimaryGold,
                        contentColor = White,
                        focusedContentColor = PureBlack
                    ),
                    scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
                    border = ClickableSurfaceDefaults.border(
                        focusedBorder = Border(BorderStroke(2.dp, White))
                    )
                ) {
                    Text(
                        text = stringResource(R.string.settings_back),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = White
                    )
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SettingsSection(title = stringResource(R.string.settings_section_playback)) {
                    SettingsInfoRow(
                        title = stringResource(R.string.settings_overlay_hide),
                        subtitle = stringResource(R.string.settings_overlay_hide_desc)
                    )
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_low_latency),
                        subtitle = stringResource(R.string.settings_low_latency_desc),
                        checked = state.lowLatencyMode,
                        onCheckedChange = { viewModel.setLowLatency(it) },
                        onLabel = stringResource(R.string.settings_toggle_on),
                        offLabel = stringResource(R.string.settings_toggle_off)
                    )
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_hw_decode),
                        subtitle = stringResource(R.string.settings_hw_decode_desc),
                        checked = state.hardwareDecoder,
                        onCheckedChange = { viewModel.setHardwareDecoder(it) },
                        onLabel = stringResource(R.string.settings_toggle_on),
                        offLabel = stringResource(R.string.settings_toggle_off)
                    )
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_highest_quality),
                        subtitle = stringResource(R.string.settings_highest_quality_desc),
                        checked = state.preferHighestQuality,
                        onCheckedChange = { viewModel.setPreferHighestQuality(it) },
                        onLabel = stringResource(R.string.settings_toggle_on),
                        offLabel = stringResource(R.string.settings_toggle_off)
                    )
                }

                SettingsSection(title = stringResource(R.string.settings_section_privacy)) {
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_analytics),
                        subtitle = stringResource(R.string.settings_analytics_desc),
                        checked = state.analyticsEnabled,
                        onCheckedChange = { viewModel.setAnalytics(it) },
                        onLabel = stringResource(R.string.settings_toggle_on),
                        offLabel = stringResource(R.string.settings_toggle_off)
                    )
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_parental),
                        subtitle = stringResource(R.string.settings_parental_desc),
                        checked = state.parentalControls,
                        onCheckedChange = { viewModel.setParental(it) },
                        onLabel = stringResource(R.string.settings_toggle_on),
                        offLabel = stringResource(R.string.settings_toggle_off)
                    )
                }

                SettingsSection(title = stringResource(R.string.settings_section_data)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.settings_clear_cache_desc),
                            style = MaterialTheme.typography.labelLarge,
                            color = White.copy(alpha = 0.45f)
                        )
                        Surface(
                            onClick = {
                                try {
                                    context.cacheDir.deleteRecursively()
                                    context.codeCacheDir?.deleteRecursively()
                                } catch (_: Exception) { }
                            },
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Color(0x33FF6B6B),
                                focusedContainerColor = Color(0xFFFF5555),
                                contentColor = White
                            ),
                            border = ClickableSurfaceDefaults.border(
                                focusedBorder = Border(BorderStroke(2.dp, White))
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.settings_clear_cache),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            )
                        }
                    }
                }

                SettingsSection(title = stringResource(R.string.settings_section_account)) {
                    Text(
                        text = stringResource(R.string.settings_sign_out_desc),
                        style = MaterialTheme.typography.labelLarge,
                        color = White.copy(alpha = 0.45f)
                    )
                    Surface(
                        onClick = onSignOut,
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Color(0x33FFAA44),
                            focusedContainerColor = PrimaryGold,
                            contentColor = White,
                            focusedContentColor = PureBlack
                        ),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, White))
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.settings_sign_out),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }

                SettingsSection(title = stringResource(R.string.settings_section_about)) {
                    Text(
                        text = stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
                        style = MaterialTheme.typography.bodyLarge,
                        color = PrimaryGold.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = SurfaceDefaults.colors(containerColor = Color(0x990F1419)),
        border = Border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)))
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = PrimaryGold
                )
            )
            content()
        }
    }
}

@Composable
private fun SettingsInfoRow(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = White
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelLarge,
            color = White.copy(alpha = 0.45f)
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLabel: String,
    offLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = White.copy(alpha = 0.42f)
            )
        }
        Surface(
            onClick = { onCheckedChange(!checked) },
            modifier = Modifier
                .width(108.dp)
                .height(44.dp),
            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = if (checked) PrimaryGold.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.1f),
                focusedContainerColor = PrimaryGold.copy(alpha = 0.75f),
                contentColor = White,
                focusedContentColor = PureBlack
            ),
            border = ClickableSurfaceDefaults.border(
                focusedBorder = Border(BorderStroke(2.dp, White))
            )
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (checked) onLabel else offLabel,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
