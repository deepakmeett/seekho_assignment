package com.ds.seekhoassignment.ui.screen

import android.R
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.viewModel.AnimeUiEffect
import com.ds.seekhoassignment.data.viewModel.AnimeUiEvent
import com.ds.seekhoassignment.data.viewModel.AnimeViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(navController: NavHostController, animeId: Int = 0) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<AnimeViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val data = uiState.animeDetailsData?.data

    val effectFlow = viewModel.effect

    LaunchedEffect(animeId) {
        viewModel.setEvent(AnimeUiEvent.LoadAnimeById(animeId))

        effectFlow.collect { effect ->
            when (effect) {
                is AnimeUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (data != null) {
            VideoOrThumbnail(data, navController)
            AnimeData(data)
        }
    }
}

@Composable
private fun VideoOrThumbnail(data: Data?, navController: NavHostController) {

    Box {
        if (data?.trailer?.youtubeId.toString() != "null") {
            Column {
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))   // Handles dynamic status bar height across devices
                YoutubeVideoPlayer(data?.trailer?.youtubeId.toString())
            }
        } else if (data?.trailer?.images?.largeImageUrl.toString() != "null") {
            ThumbnailImage(data?.trailer?.images?.largeImageUrl)
        } else {
            ThumbnailImage(data?.images?.jpg?.largeImageUrl)
        }

        IconButton(
            modifier = Modifier
                .padding(top = 30.dp, start = 10.dp)
                .background(
                    color = Color(0x33BDB2B2),
                    shape = MaterialTheme.shapes.medium,
                ),
            onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }


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
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrl,
                placeholder = painterResource(id = R.drawable.ic_media_play),
                error = painterResource(id = R.drawable.stat_notify_error)
            ),
            contentDescription = "Poster image",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00000000),
                            Color(0x80000000),
                            Color(0xFF000000)
                        )
                    )
                )
                .height(100.dp)

        )
    }

}

@SuppressLint("DefaultLocale")
@Composable
private fun AnimeData(data: Data?) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
//                        Color(0xFF000000),
                        Color(0xFF2D2B28)
                    )
                )
            )
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 30.dp)
        ) {
            Text(
                text = data?.title.toString().trim(),
                color = Color(0xffCCCCCC),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 40.sp
                ),
                modifier = Modifier.wrapContentHeight(),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Episodes: ",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF9B9797),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "${data?.episodes}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp
                    ),
                    color = Color(0xffCCCCCC),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                Box(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .width(1.5.dp)
                        .height(15.dp)
                        .background(Color(0xffCCCCCC))
                )

                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star $i",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 2.dp)

                    )
                }

                Text(
                    text = String.format("%.1f", data?.score ?: 0f),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xffCCCCCC),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(5.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {

                items(data?.genres?.size ?: 0) { index ->
                    val genreData = data?.genres?.get(index)

                    Text(
                        modifier = Modifier
                            .padding(end = 7.dp)
                            .widthIn(max = 140.dp)
                            .background(
                                color = Color(0xFF414444),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 5.dp),
                        text = genreData?.name.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = Color(0xffCCCCCC),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Synopsis",
                color = Color(0xffCCCCCC),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = data?.synopsis.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF9B9797),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewDetailsScreen() {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        VideoOrThumbnail(null, rememberNavController())
        AnimeData(null)
    }
}



