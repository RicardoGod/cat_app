package com.example.cat_app.data.api.dto.favourite

import com.example.cat_app.data.models.FavouriteRequestModel
import com.google.gson.annotations.SerializedName

data class FavouriteRequest (
    @SerializedName("image_id") val imageId: String
){
    class Factory {
        companion object {
            fun fromFavouriteRequest(request: FavouriteRequestModel): FavouriteRequest {
                return FavouriteRequest(request.imageId)
            }
        }
    }
}