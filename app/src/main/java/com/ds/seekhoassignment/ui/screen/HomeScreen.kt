package com.ds.seekhoassignment.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ds.seekhoassignment.data.viewModel.AnimeUiEffect
import com.ds.seekhoassignment.data.viewModel.AnimeUiEvent
import com.ds.seekhoassignment.data.viewModel.AnimeUiState
import com.ds.seekhoassignment.data.viewModel.AnimeViewModel
import com.ds.seekhoassignment.ui.NavRoutes
import com.ds.seekhoassignment.ui.theme.SeekhoAssignmentTheme


@Composable
internal fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<AnimeViewModel>()

    val uiState by viewModel.uiState.collectAsState()

    val effectFlow = viewModel.effect

    val isConnected = viewModel.isConnected.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.setEvent(AnimeUiEvent.LoadAnimeList)

        effectFlow.collect { effect ->
            when (effect) {
                is AnimeUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (!isConnected.value) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(color = Color(0xFFFF0000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    text = "Connection lost..."
                )
            }
        }
        ListDataCompose(
            uiState,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFF000000)),
            navController
        )
    }
}


@Composable
fun ListDataCompose(
    uiState: AnimeUiState,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!uiState.animeListData.isNullOrEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Header("Popular Anime")
            }

            items(uiState.animeListData?.size ?: 0) { index ->
                val data = uiState.animeListData?.get(index)
                AnimeCard(
                    data?.title.orEmpty(),
                    data?.score.toString(),
                    data?.genres?.firstOrNull()?.name.toString(),
                    data?.titles?.size ?: 0,
                    data?.images?.jpg?.largeImageUrl.orEmpty()
                ) {
                    navController.navigate("${NavRoutes.DETAILS_SCREEN}/${data?.malId ?: ""}")
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
            text = title,
            color = Color(0xffCCCCCC),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp
            ),
            modifier = Modifier.wrapContentHeight(),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun AnimeCard(
    name: String,
    score: String,
    genre: String,
    episodes: Int,
    image: String,
    click: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                click()
            },
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = Color(0xFF2D2B28),
                    shape = MaterialTheme.shapes.medium
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF2D2B28)
                        )
                    )
                )
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
                        color = Color(0xffCCCCCC),
                        maxLines = 1,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .widthIn(max = 140.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF414444),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 5.dp, vertical = 3.dp),
                            text = genre,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC3C3C3),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )

                        Row(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF414444),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 5.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier
                                    .padding(end = 2.dp)
                                    .size(12.dp)
                            )
                            Text(
                                text = String.format("%.1f", score.toFloatOrNull() ?: 0f),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xffC3C3C3),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row {
                        Text(
                            text = "Episodes: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF807C7C),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = "$episodes",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffCCCCCC),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )


                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    SeekhoAssignmentTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF000000)),
            verticalArrangement = Arrangement.Center
        ) {
            Header("Popular Anime ")
            AnimeCard(
                "Friends",
                "9.3",
                "Comedy",
                10,
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}
            AnimeCard(
                "John Wick",
                "8.2",
                "Action",
                13,
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}
            AnimeCard(
                "Harry Potter",
                "7.5",
                "Drama",
                14,
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}

        }
    }
}
