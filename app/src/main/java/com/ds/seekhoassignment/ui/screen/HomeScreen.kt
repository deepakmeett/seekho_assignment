package com.ds.seekhoassignment.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.viewModel.AnimeViewModel
import com.ds.seekhoassignment.ui.NavRoutes
import com.ds.seekhoassignment.ui.theme.SeekhoAssignmentTheme


@Composable
internal fun HomeScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<AnimeViewModel>()

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ListDataCompose(
            uiState.animeListData,
            modifier = Modifier.padding(innerPadding),
            navController
        )
    }
}


@Composable
fun ListDataCompose(
    animeList: List<Data>?,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    if (animeList.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Header("Popular Anime")
            }

            items(animeList.size) { index ->
                val data = animeList[index]
                AnimeCard(
                    data.title.orEmpty(),
                    data.score.toString(),
                    data.titles?.size ?: 0,
                    data.images?.jpg?.largeImageUrl.orEmpty()
                ) {
                    navController.navigate("${NavRoutes.DETAILS_SCREEN}/${data.trailer?.youtubeId ?: ""}")
                }
            }
        }
    }
}

@Composable
private fun Header(title: String) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun AnimeCard(name: String, score: String, episodes: Int, image: String, click: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                click()
            },
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = image,
                    placeholder = painterResource(id = android.R.drawable.ic_media_play),
                    error = painterResource(id = android.R.drawable.stat_notify_error)
                ),
                contentDescription = "Poster image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = score,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    text = "Episodes: $episodes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    SeekhoAssignmentTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Header("Popular Anime ")
            AnimeCard(
                "Sousa no Friend",
                "9.3",
                10,
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}

        }
    }
}
