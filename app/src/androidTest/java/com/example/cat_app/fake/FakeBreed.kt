package com.example.cat_app.fake

import com.example.cat_app.ui.features.breeds.model.BreedUi

object BreedFakes {

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

    val siamese = BreedUi(
        id = "2",
        name = "Siamese",
        origin = "Thailand",
        temperament = "Affectionate",
        lifeSpan = "8 - 15",
        weight = BreedUi.BreedsWeightUi(
            imperial = "kg",
            metric = "5 - 6.5"
        ),
        imageUrl = BreedUi.BreedsImageUi(
            url = ""
        ),
        isFavorite = false,
        description = "They are affectionate and social and can easily adapt to family life."
    )

    val bengal = BreedUi(
        id = "3",
        name = "Bengal",
        origin = "USA",
        temperament = "Independent",
        lifeSpan = "10 - 16",
        weight = BreedUi.BreedsWeightUi(
            imperial = "kg",
            metric = "3.6 - 7.7"
        ),
        imageUrl = BreedUi.BreedsImageUi(
            url = ""
        ),
        isFavorite = false,
        description = "Muscular cat with a wild appearance."
    )
}