package com.example.cat_app.data.api.dto.cat

import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.BreedsModel.BreedsImageModel
import com.example.cat_app.data.models.BreedsModel.BreedsWeightModel
import com.google.gson.annotations.SerializedName

data class CatDto(
    val id: String? = null,
    val name: String? = null,
    val origin: String? = null,

    val temperament: String? = null,
    val description: String? = null,

    @SerializedName("life_span")
    val lifeSpan: String? = null,

    @SerializedName("reference_image_id")
    val referenceImageId: String? = null,

    val image: CatImageDto? = null,

    val weight: CatWeightDto? = null,
){

    fun toBreedsModel(): BreedsModel {
        return BreedsModel(
            id = id,
            name = name,
            origin = origin,
            temperament = temperament,
            description = description ,
            lifeSpan = lifeSpan,
            referenceImageId = referenceImageId,
            image = image?.toBreedsImageModel(),
            weight = weight?.toBreedsWeightModel()
        )
    }

    data class CatWeightDto (
        val imperial: String? = null,
        val metric: String? = null
    ) {
        fun toBreedsWeightModel(): BreedsWeightModel {
            return BreedsWeightModel(
                imperial = imperial,
                metric = metric
            )
        }
    }

    data class CatImageDto (
        val id: String? = null,
        val url: String? = null,
        val width: Int? = null,
        val height: Int? = null
    ){

        fun toBreedsImageModel(): BreedsImageModel {
            return BreedsImageModel(
                id = id,
                url = url,
                width = width,
                height = height
            )
        }
    }
}