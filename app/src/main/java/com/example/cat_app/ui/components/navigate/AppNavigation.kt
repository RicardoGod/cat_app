package com.example.cat_app.ui.components.navigate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cat_app.ui.screen.ScreenBreeds
import com.example.cat_app.ui.screen.ScreenFavorites
import com.example.cat_app.ui.screen.ScreenOnboard
import com.example.cat_app.ui.screen.ScreenSplash
import com.example.cat_app.viewmodel.BreedsViewModel
import com.example.cat_app.viewmodel.FavouritesViewModel
import com.example.cat_app.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            ScreenSplash(
                navigateToNextScreen = {
                    navController.navigate("onboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboard") {
            ScreenOnboard(
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToBreeds = { navController.navigate("list") }
            )
        }

        composable("list") {
            val viewModel: BreedsViewModel = koinViewModel()

            ScreenBreeds(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("favorites") {
            val viewModel: FavouritesViewModel = koinViewModel()
            val searchModel : SearchViewModel = koinViewModel()

            ScreenFavorites(
                favouriteViewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                searchViewModel = searchModel
            )
        }
    }
}