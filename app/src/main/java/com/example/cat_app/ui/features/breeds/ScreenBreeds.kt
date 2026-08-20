package com.example.cat_app.ui.features.breeds

import ads_mobile_sdk.h2
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.breeds.components.BreedDialog
import com.example.cat_app.ui.features.breeds.components.BreedList
import com.example.cat_app.ui.features.breeds.model.BreedsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBreeds(
    state: BreedsUiState,
    onEvent: (BreedsEvent) -> Unit,
    navigateBack: () -> Unit
) {

    LaunchedEffect(Unit) {
        onEvent(BreedsEvent.LoadingScreen)
    }

    TextField(
        value = state.search,
        onValueChange = {
            onEvent(BreedsEvent.SearchChanged(it))
        }
    )

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
            //search bar
            OutlinedTextField(
                value = state.search,
                onValueChange = { onEvent(BreedsEvent.SearchChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.SEARCH_FIELD)
                    .padding(16.dp),
                label = { Text("Search breed...") },
                singleLine = true,
                trailingIcon = {
                    if (state.search.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onEvent(BreedsEvent.ClearSearch)
                            }
                        ) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

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
                        onEvent(BreedsEvent.BreedClicked(breed))
                    },
                    onFavoriteClick = { breed ->
                        onEvent(BreedsEvent.ToggleFavorite(breed))

                    }
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(TestTags.LOADING)
                )
            }

        }
        //card with details about the cat
        if (state.selectedBreed != null) {
            BreedDialog(
                breed = state.selectedBreed!!,
                onDismiss = {
                    onEvent(BreedsEvent.CloseDialog)
                }
            )
        }
    }

}