package com.example.cat_app.ui.features.breeds.model

import com.example.cat_app.data.models.BreedsModel

data class BreedUi(
    val id: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val lifeSpan: String,
    val weight: BreedsWeightUi,
    val imageUrl: BreedsImageUi,
    var isFavorite: Boolean,
    val description: String
) {

    companion object {
        fun fromBreedsModel(breedsModel: BreedsModel, isFavorite: Boolean): BreedUi {
            return BreedUi(
                id = breedsModel.id ?: "",
                name = breedsModel.name ?: "",
                origin = breedsModel.origin ?: "",
                temperament = breedsModel.temperament ?: "",
                lifeSpan = breedsModel.lifeSpan ?: "",
                weight = BreedsWeightUi.fromBreedsWeightModel(breedsModel.weight),
                imageUrl = BreedsImageUi.fromBreedsImageModel(breedsModel.image),
                description = breedsModel.description ?: "",
                isFavorite = isFavorite
            )
        }
    }

    data class BreedsImageUi(
        val id: String? = null,
        val url: String? = null,
        val width: Int? = null,
        val height: Int? = null
    ) {

        companion object {
            fun fromBreedsImageModel(breedsImageModel: BreedsModel.BreedsImageModel?): BreedsImageUi {
                return BreedsImageUi(
                    id = breedsImageModel?.id ?: "",
                    url = breedsImageModel?.url ?: "",
                    width = breedsImageModel?.width ?: 0,
                    height = breedsImageModel?.height ?: 0
                )
            }
        }
    }

    data class BreedsWeightUi(
        val imperial: String? = null,
        val metric: String? = null
    ) {
        companion object {
            fun fromBreedsWeightModel(breedsWeightModel: BreedsModel.BreedsWeightModel?): BreedsWeightUi {
                return BreedsWeightUi(
                    imperial = breedsWeightModel?.imperial ?: "",
                    metric = breedsWeightModel?.metric ?: ""
                )
            }
        }
    }
}