package com.example.cat_app.ui.features.breeds

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cat_app.ui.features.breeds.model.BreedUi

@Preview
@Composable
fun ScreenBreedsPreview() {
    ScreenBreeds(
        state = BreedsUiState(
            getBreeds(),
            null,
            "",
            false,
            null
        ),
        onEvent = { } ,
        navigateBack = {}
    )
}

private fun getBreeds() = listOf(
    BreedUi(
        id = "1",
        name = "Abyssinian",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        origin = "Egypt",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        lifeSpan = "14",
        weight = BreedUi.BreedsWeightUi(
            imperial = "7 - 10",
            metric = "3 - 5"
        ),
        isFavorite = true,
        imageUrl = BreedUi.BreedsImageUi(
            url = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
    )
)
