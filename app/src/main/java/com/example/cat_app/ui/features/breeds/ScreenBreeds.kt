package com.example.cat_app.ui.features.breeds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cat_app.ui.features.breeds.components.BreedDialog
import com.example.cat_app.ui.features.breeds.components.BreedList

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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                        style = MaterialTheme.typography.h2,
                        color = Color.Gray
                    )
                }
            } else {
                //list of found it
                LazyColumn {
                    items(state.breeds) { breed ->
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
                }
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