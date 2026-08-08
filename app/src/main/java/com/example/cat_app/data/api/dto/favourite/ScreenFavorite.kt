package com.example.cat_app.data.api.dto.favourite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.breeds.BreedsEvent
import com.example.cat_app.ui.features.breeds.components.BreedList
import com.example.cat_app.ui.features.breeds.model.BreedsUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenFavorite(
    state: BreedsUiState,
    onEvent: (FavoriteEvent) -> Unit,
    navigateBack: () -> Unit
) {

    LaunchedEffect(Unit) {
        onEvent(FavoriteEvent.LoadingScreen)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🐾 List of Cats") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.breeds.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No breeds found 😿",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Gray
                    )
                }
            } else {
                //list of found it

                BreedList(
                    breeds = state.breeds,
                    onBreedClick = { breed ->
                        onEvent(FavoriteEvent.BreedClicked(breed))
                    },
                    onFavoriteClick = { breed ->
                        onEvent(FavoriteEvent.CloseDialog)

                    }
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(TestTags.LOADING)
                )
            }


        }
    }
}