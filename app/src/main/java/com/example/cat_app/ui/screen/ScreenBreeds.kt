package com.example.cat_app.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.cat_app.ui.components.events.BreedsEvent
import com.example.cat_app.viewmodel.state.BreedsUiState

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenBreeds(
    state : BreedsUiState,
    onEvent: BreedsEvent -> Unit
){
    fun onEvent(event: BreedsEvent){
        when (event) {
            is BreedsEvent.SearchChanged -> {

            }
        }
    }
}