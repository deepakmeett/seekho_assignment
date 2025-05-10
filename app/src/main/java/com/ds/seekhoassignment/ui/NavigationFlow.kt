package com.ds.seekhoassignment.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ds.seekhoassignment.ui.screen.DetailsScreen
import com.ds.seekhoassignment.ui.screen.HomeScreen

const val NAV_PARAM_ANIME_ID = "animeId"

object NavRoutes {
    const val HOME_SCREEN = "home"
    const val DETAILS_SCREEN = "details"
    const val DETAILS_SCREEN_WITH_ARG = "$DETAILS_SCREEN/{$NAV_PARAM_ANIME_ID}"
}

@Composable
internal fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = NavRoutes.HOME_SCREEN,
        modifier = Modifier
            .fillMaxSize()
    ) {

        composable(route = NavRoutes.HOME_SCREEN) {
            HomeScreen(navController)
        }

        composable(
            route = NavRoutes.DETAILS_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(NAV_PARAM_ANIME_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(NAV_PARAM_ANIME_ID) ?: 0
            DetailsScreen(navController = navController, animeId = animeId)
        }
    }
}
