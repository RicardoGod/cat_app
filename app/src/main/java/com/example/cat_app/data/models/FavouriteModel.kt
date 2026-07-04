package com.example.cat_app.data.models

data class FavouriteModel(
    val id: Int,
    val imageId: String,
    val subId: String? = null,
    val createdAt: String
)