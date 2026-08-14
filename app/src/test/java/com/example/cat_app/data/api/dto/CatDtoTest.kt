package com.example.cat_app.data.api.dto

import com.example.cat_app.helper.FakeCatDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CatDtoTest {

    @Test
    fun toBreedsModel_mapsAllFields() {

        val dto = FakeCatDto.persian

        val result = dto.toBreedsModel()

        assertEquals(dto.id, result.id)
        assertEquals(dto.name, result.name)
        assertEquals(dto.origin, result.origin)
        assertEquals(dto.temperament, result.temperament)
        assertEquals(dto.description, result.description)
        assertEquals(dto.lifeSpan, result.lifeSpan)
        assertEquals(dto.weight?.metric, result.weight?.metric)
        assertEquals(dto.weight?.imperial, result.weight?.imperial)
        assertEquals(dto.image?.url, result.image?.url)
    }

    @Test
    fun toBreedsModel_mapsNullImage() {

        val dto = FakeCatDto.persian.copy(
            image = null
        )

        val result = dto.toBreedsModel()

        assertEquals(null, result.image)
    }


}