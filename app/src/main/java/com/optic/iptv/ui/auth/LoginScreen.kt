package com.optic.iptv.ui.auth

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
                        colors = listOf(Color(0x3300A3FF), PureBlack)
                    )
                )
                .blur(80.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = PrimaryGold,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.headlineLarge,
                color = White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Surface(
                modifier = Modifier
                    .width(600.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = SurfaceDefaults.colors(
                    containerColor = GlassBackground
                ),
                border = Border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.code.ifBlank { codePlaceholder },
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 8.sp,
                            color = if (state.code.isBlank()) White.copy(alpha = 0.2f) else White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    val keypadClearLabel = stringResource(R.string.keypad_clear)
                    val keypadOkLabel = stringResource(R.string.keypad_ok)
                    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "CLR", "0", "OK")
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(240.dp).width(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(keys.size) { index ->
                            val key = keys[index]
                            KeypadButton(
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
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    isPrimaryAction: Boolean = false,
    isLoading: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1.5f),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.05f),
            focusedContainerColor = PrimaryGold,
            pressedContainerColor = PrimaryGold.copy(alpha = 0.8f),
            contentColor = White,
            focusedContentColor = PureBlack
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.15f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(BorderStroke(2.dp, White))
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
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
