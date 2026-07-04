package com.example.cat_app.data.api.dto.favourite

import com.example.cat_app.data.models.FavouriteModel
import com.google.gson.annotations.SerializedName

data class FavouriteDto (
    val id: Int,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("sub_id") val subId: String? = null,
    @SerializedName("created_at") val createdAt: String
){
    fun toFavouriteModel() : FavouriteModel {
      return FavouriteModel(
          id = id,
          imageId = imageId,
          subId = subId,
          createdAt = createdAt
      )
    }
}