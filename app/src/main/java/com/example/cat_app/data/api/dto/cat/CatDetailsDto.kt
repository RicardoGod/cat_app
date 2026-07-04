package com.example.cat_app.data.api.dto.cat

data class CatDetailsDto(
    val id: Int,
    val name: String,
    val weight: String,
    val height: String,
    val life_span: String,
    val bred_for: String?,
    val breed_group: String?
)