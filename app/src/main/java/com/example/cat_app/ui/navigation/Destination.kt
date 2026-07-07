package com.example.cat_app.ui.navigation

sealed class Destination(
    val route: String
) {

    data object Splash : Destination("splash")

    data object Onboard : Destination("onboard")

    data object Breeds : Destination("breeds")

    data object Favorites : Destination("favorites")
}