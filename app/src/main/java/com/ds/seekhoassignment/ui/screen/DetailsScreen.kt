package com.ds.seekhoassignment.ui.screen

import android.R
import android.R.attr.data
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.viewModel.AnimeUiEvent
import com.ds.seekhoassignment.data.viewModel.AnimeViewModel
import com.ds.seekhoassignment.ui.NavRoutes
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(navController: NavHostController, animeId: Int = 0) {
    val viewModel = hiltViewModel<AnimeViewModel>()

    val uiState by viewModel.uiState.collectAsState()
    val data = uiState.animeDetailsData?.data

    LaunchedEffect(animeId) {
        viewModel.setEvent(AnimeUiEvent.LoadAnimeById(animeId))
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Details Screen") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        VideoOrThumbnail(data)
        AnimeData(data)
    }
}

@Composable
private fun VideoOrThumbnail(data: Data?) {
    if (data?.trailer?.youtubeId.toString() != "null") {
        YoutubeVideoPlayer(data?.trailer?.youtubeId.toString())
    } else if (data?.trailer?.images?.largeImageUrl.toString() != "null") {
        ThumbnailImage(data?.trailer?.images?.largeImageUrl)
    } else {
        ThumbnailImage(data?.images?.jpg?.largeImageUrl)
    }
}

@Composable
fun YoutubeVideoPlayer(
    videoId: String
) {
    AndroidView(factory = {
        var view = YouTubePlayerView(it)
        val fragment = view.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        )
        view
    })
}

@Composable
private fun ThumbnailImage(imageUrl: String?) {
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.ic_media_play),
            error = painterResource(id = R.drawable.stat_notify_error)
        ),
        contentDescription = "Poster image",
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}

@Composable
private fun AnimeData(data: Data?) {
    Text(
        text = data?.title.toString(),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        fontSize = 16.sp,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )

    Text(
        text = data?.synopsis.toString(),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary,
        maxLines = 5,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Start,
    )

    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {

        items(data?.genres?.size ?: 0) { index ->
            val genreData = data?.genres?.get(index)

            Text(
                modifier = Modifier.padding(5.dp),
                text = genreData?.name.toString(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )

        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = data?.score.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
    Text(
        text = "Episodes: ${data?.titles?.size}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewDetailsScreen() {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Details Screen") },
            navigationIcon = {
                IconButton(onClick = {
//                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        VideoOrThumbnail(null)
        AnimeData(null)
    }

}



