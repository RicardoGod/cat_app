package com.example.cat_app.helper

import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.ui.features.breeds.model.BreedUi

object FakeBreedsModel {

    val persian = BreedsModel(
        id = "1",
        name = "Persian",
        origin = "Iran",
        temperament = "Affectionate",
        lifeSpan = "12 - 15",
        weight = BreedsModel.BreedsWeightModel(
            imperial = "kg",
            metric = "4 - 7"
        ),
        image = BreedsModel.BreedsImageModel(
            url = "",
            id = "1"
        ),
        description = "A calm and affectionate breed."
    )

    val bengal = BreedsModel(
        id = "3",
        name = "Bengal",
        origin = "USA",
        temperament = "Independent",
        lifeSpan = "10 - 16",
        weight = BreedsModel.BreedsWeightModel(
            imperial = "kg",
            metric = "3.6 - 7.7"
        ),
        image = BreedsModel.BreedsImageModel(
            url = "",
            id = "3"
        ),
        description = "Muscular cat with a wild appearance."
    )



}