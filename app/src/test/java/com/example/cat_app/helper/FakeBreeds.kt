package com.example.cat_app.helper

import com.example.cat_app.ui.features.breeds.model.BreedUi

object FakeBreeds {

    val persian = BreedUi(
        id = "1",
        name = "Persian",
        origin = "Iran",
        temperament = "Affectionate",
        lifeSpan = "12 - 15",
        weight = BreedUi.BreedsWeightUi(
            imperial = "kg",
            metric = "4 - 7"
        ),
        imageUrl = BreedUi.BreedsImageUi(
            url = ""
        ),
        isFavorite = false,
        description = "A calm and affectionate breed."
    )
}