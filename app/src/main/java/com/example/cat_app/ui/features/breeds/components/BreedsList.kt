package com.example.cat_app.ui.features.breeds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cat_app.ui.features.breeds.model.BreedUi
import androidx.compose.foundation.lazy.items

@Composable
fun BreedList(
    breeds: List<BreedUi>,
    onBreedClick: (BreedUi) -> Unit,
    onFavoriteClick: (BreedUi) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(breeds) { breed ->

            BreedItemCard(
                breed = breed,
                onCardClick = {
                    onBreedClick(breed)
                },
                onFavoriteClick = {
                    onFavoriteClick(breed)
                }
            )
        }
    }
}