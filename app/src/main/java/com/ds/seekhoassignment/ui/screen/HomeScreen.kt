package com.ds.seekhoassignment.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Popular anime",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            items(animeList.size) { index ->
                val data = animeList[index]
                AnimeCard(
                    data.title.orEmpty(),
                    data.synopsis.orEmpty(),
                    data.images?.jpg?.largeImageUrl.orEmpty()
                ) {
                    navController.navigate("${NavRoutes.DETAILS_SCREEN}/${data.malId}")
                }
            }
        }
    }
}


@Composable
fun AnimeCard(name: String, description: String, image: String, click: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
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
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .height(120.dp)
                    .width(100.dp),
                contentScale = ContentScale.Fit,
            )
            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 25.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Popular anime",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            AnimeCard(
                "Sousou no Frieren",
                "uring their decade-long quest to defeat the Demon King, the members of the hero's party—Himmel himself, the priest Heiter, the dwarf warrior Eisen, and the elven mage Frieren—forge bonds through adventures and battles",
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}
            AnimeCard(
                "Sousou no Frieren",
                "uring their decade-long quest to defeat the Demon King, the members of the hero's party—Himmel himself, the priest Heiter, the dwarf warrior Eisen, and the elven mage Frieren—forge bonds through adventures and battles",
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg"
            ) {}
        }
    }
}
