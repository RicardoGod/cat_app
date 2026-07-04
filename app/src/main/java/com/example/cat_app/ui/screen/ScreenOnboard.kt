package com.example.cat_app.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.cat_app.viewmodel.BreedsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenOnboard(
    viewModel: BreedsViewModel,
    onNavigateToBreeds: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {

}