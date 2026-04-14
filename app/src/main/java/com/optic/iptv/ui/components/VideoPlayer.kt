package com.optic.iptv.ui.components

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.optic.iptv.R

@OptIn(UnstableApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    showController: Boolean = false,
    controllerShowTimeoutMs: Int = 5_000
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val invalidUrlMessage = stringResource(R.string.playback_invalid_url)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val exoPlayer = remember(appContext) {
        ExoPlayer.Builder(appContext)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        val msg = context.getString(
                            R.string.playback_error,
                            error.localizedMessage ?: error.message.orEmpty()
                        )
                        ContextCompat.getMainExecutor(context).execute {
                            errorMessage = msg
                        }
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_READY) {
                            ContextCompat.getMainExecutor(context).execute {
                                errorMessage = null
                            }
                        }
                    }
                })
            }
    }

    fun isPlayableUrl(raw: String): Boolean {
        val u = raw.trim()
        if (u.isEmpty()) return false
        val uri = try {
            Uri.parse(u)
        } catch (_: Exception) {
            return false
        }
        val scheme = uri.scheme?.lowercase()
        return scheme == "http" || scheme == "https" || scheme == "rtmp" || scheme == "rtsp"
    }

    LaunchedEffect(url) {
        errorMessage = null
        exoPlayer.stop()
        if (url.isNotBlank()) {
            try {
                exoPlayer.setMediaItem(MediaItem.fromUri(url))
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            } catch (e: Exception) {
                errorMessage = context.getString(
                    R.string.playback_error,
                    e.localizedMessage ?: e.message.orEmpty()
                )
            }
        } else {
            errorMessage = context.getString(R.string.playback_invalid_url)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = showController
                    setControllerShowTimeoutMs(controllerShowTimeoutMs)
                    setControllerHideOnTouch(false)
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { view ->
                view.useController = showController
                view.setControllerShowTimeoutMs(controllerShowTimeoutMs)
                if (view.player !== exoPlayer) view.player = exoPlayer
            },
            onRelease = { view ->
                view.player = null
                exoPlayer.release()
            },
            modifier = Modifier.fillMaxSize()
        )

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
