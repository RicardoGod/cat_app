package com.example.cat_app.ui.features.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cat_app.ui.features.breeds.components.BreedDialog
import com.example.cat_app.ui.features.breeds.components.BreedItemCard
import com.example.cat_app.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenFavourites(
    favouriteViewModel: FavouritesViewModel = koinViewModel(),
    searchViewModel: SearchViewModel,
    navigateBack: () -> Unit = {}) {

    //current context of the composable
    val context = LocalContext.current

    //current text entered in the search bar
    var searchQuery by remember { searchViewModel.searchQuery }

    var favouriteScreenState by remember {favouriteViewModel.state }

    var scrollState = rememberLazyListState()

    //controls visibility of the detail dialog
    var showDialog by remember { mutableStateOf(favouriteViewModel.state.value.selected) }


    //clears search data when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            searchViewModel.clearSearch()
        }
    }
    //loads initial data when the screen is first displayed
    LaunchedEffect(Unit) {
        favouriteViewModel.fetchFavorites(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💜 Favorites") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            favouriteScreenState.data.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No favorites added yet 😿", style = MaterialTheme.typography.titleMedium)
                }
            }
            else -> {
                //list of found it
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(favouriteScreenState.data.size) {
                        fav ->
                        val breed = favouriteScreenState.data.find { it.imageId == fav.imageId }
                        val isFavorite =
                            favorites_viewmodel.any { it.imageId == breed?.referenceImageId }
                        if (breed != null) {
                            //card displaying cat breed data fetched from the API
                            BreedItemCard(
                                breed = breed,
                                onFavoriteClick = {
                                    if (isFavorite) {
                                        favouriteViewModel.removeFavorite(context, breed)
                                    } else {
                                        favouriteViewModel.addFavorite(context, breed)
                                    }
                                },
                                isFavorite = isFavorite,
                                viewModel = favouriteViewModel,
                                onClick = {
                                    selectedBreed = breed
                                    showDialog = true
                                }
                            )
                        }
                    }
                }
                //card with details about the cat
                if (showDialog && selectedBreed != null) {
                    val isFavorite =
                        favorites_viewmodel.any { it.imageId == selectedBreed!!.referenceImageId }
                    BreedDialog(
                        breed = selectedBreed!!,
                        viewModel = favouriteViewModel,
                        isFavorite = isFavorite,
                        onDismiss = { showDialog = false }
                    )

                }
            }
        }
    }
}