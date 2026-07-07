package com.example.cat_app.ui.features.breeds

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenBreeds(
    state : BreedsUiState,
    onEvent: (BreedsEvent) -> Unit
){

    TextField(
        value = state.search,
        onValueChange = {
            onEvent(BreedsEvent.SearchChanged(it))
        }
    )

}