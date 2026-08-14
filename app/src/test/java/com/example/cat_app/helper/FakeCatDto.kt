package com.example.cat_app.helper

import com.example.cat_app.data.api.dto.cat.CatDto

object FakeCatDto {

    val persian = CatDto(
        id = "1",
        name = "Persian",
        origin = "Iran",
        temperament = "Affectionate",
        lifeSpan = "12 - 15",
        weight = CatDto.CatWeightDto(
            imperial = "kg",
            metric = "4 - 7"
        ),
        image = CatDto.CatImageDto(
            url = "",
            id = "1"
        ),
        description = "A calm and affectionate breed.")
}