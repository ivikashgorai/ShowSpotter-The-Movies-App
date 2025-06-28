package com.example.showspotter.designs

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.rememberAsyncImagePainter
import com.example.showspotter.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayerTrailer(videoId: String) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}



@Composable
fun YouTubePlayerWithPreview(videoId: String) {
    var showPlayer by remember { mutableStateOf(false) }

    if (showPlayer) {
        YouTubePlayerTrailer(videoId)
    } else {
        YouTubeThumbnail(videoId) {
            showPlayer = true
        }
    }
}

@Composable
fun YouTubeThumbnail(videoId: String, onClick: () -> Unit) {
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(thumbnailUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = R.drawable.play),
            contentDescription = "Play",
            modifier = Modifier.size(48.dp)
        )
    }
}


@Composable
fun YouTubePlayerVideos(videoId: String) {
    val context = LocalContext.current

    // YouTube Thumbnail URL
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"

    // Thumbnail with Clickable Behavior
    Box(
        modifier = Modifier // Set desired height
            .clickable {
                // Intent to open YouTube video
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://www.youtube.com/watch?v=$videoId".toUri()
                )
                context.startActivity(intent)
            }
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        // Load the Thumbnail Image
            Image(
                painter = rememberAsyncImagePainter(model = thumbnailUrl),
                contentDescription = "YouTube Video Thumbnail",
                modifier = Modifier.width(240.dp).height(135.dp),
                contentScale = ContentScale.Crop
            )
    }
}
@Composable
fun YouTubePlayerAllVideos(videoId: String) {
    val context = LocalContext.current

    // YouTube Thumbnail URL
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"

    // Thumbnail with Clickable Behavior
    Box(
        modifier = Modifier // Set desired height
            .clickable {
                // Intent to open YouTube video
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://www.youtube.com/watch?v=$videoId".toUri()
                )
                context.startActivity(intent)
            }
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        // Load the Thumbnail Image
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Image(
                painter = rememberAsyncImagePainter(model = thumbnailUrl),
                contentDescription = "YouTube Video Thumbnail",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Image(painter = painterResource(id = R.drawable.play),contentDescription = "play",
                modifier = Modifier.size(50.dp))
        }
    }
}




