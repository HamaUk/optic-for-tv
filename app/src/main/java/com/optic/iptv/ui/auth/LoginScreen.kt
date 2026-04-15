package com.optic.iptv.ui.auth

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import com.optic.iptv.R
import com.optic.iptv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    ),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val codePlaceholder = stringResource(R.string.code_placeholder)
    val scroll = rememberScrollState()

    if (state.isSuccess) {
        onLoginSuccess()
    }

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
                        colors = listOf(Color(0x4000A3FF), Color(0x180E1217), PureBlack)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = PrimaryGold,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    fontSize = 44.sp,
                    lineHeight = 50.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp, lineHeight = 30.sp),
                color = White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(28.dp),
                colors = SurfaceDefaults.colors(
                    containerColor = GlassBackground.copy(alpha = 0.88f)
                ),
                border = Border(BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.code.ifBlank { codePlaceholder },
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 5.sp,
                            lineHeight = 34.sp,
                            color = if (state.code.isBlank()) White.copy(alpha = 0.22f) else White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            color = Color(0xFFFF6B6B),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val keypadClearLabel = stringResource(R.string.keypad_clear)
                    val keypadOkLabel = stringResource(R.string.keypad_ok)
                    val rows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("CLR", "0", "OK")
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rows.forEach { rowKeys ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowKeys.forEach { key ->
                                    KeypadButton(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp),
                                        text = when (key) {
                                            "CLR" -> keypadClearLabel
                                            "OK" -> keypadOkLabel
                                            else -> key
                                        },
                                        onClick = {
                                            when (key) {
                                                "CLR" -> viewModel.onBackspace()
                                                "OK" -> viewModel.submitCode()
                                                else -> viewModel.onCodeDigitEntered(key)
                                            }
                                        },
                                        isPrimaryAction = key == "OK",
                                        isLoading = state.isLoading && key == "OK"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun KeypadButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isPrimaryAction: Boolean = false,
    isLoading: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.08f),
            focusedContainerColor = PrimaryGold,
            pressedContainerColor = PrimaryGold.copy(alpha = 0.82f),
            contentColor = White,
            focusedContentColor = PureBlack
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)))
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Text(stringResource(R.string.keypad_loading), color = PureBlack)
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPrimaryAction) PrimaryGold else White
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
