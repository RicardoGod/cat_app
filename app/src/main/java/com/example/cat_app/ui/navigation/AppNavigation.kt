package com.example.cat_app.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cat_app.ui.features.breeds.BreedsRoute
import com.example.cat_app.ui.features.onboard.OnboardEvent
import com.example.cat_app.ui.features.onboard.OnboardRoute
import com.example.cat_app.ui.features.splash.SplashRoute


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destination.Splash.route) {

        composable(Destination.Splash.route) {
            SplashRoute()
        }

        composable(Destination.Onboard.route) {
            OnboardRoute(
                onNavigate = { event ->
                    run {
                        when (event) {
                            OnboardEvent.NavigateToBreeds -> navController.navigate(Destination.Breeds.route)
                            OnboardEvent.NavigateToFavorites -> navController.navigate(Destination.Favorites.route)
                        }
                    }
                }
            )
        }

        composable(Destination.Breeds.route) {
            BreedsRoute(onNavigate = {
                run {
                    navController.popBackStack()
                }
            })
        }


    }

}