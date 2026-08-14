package com.example.cat_app.ui.features.breeds

import com.example.cat_app.helper.FakeBreedsModel
import com.example.cat_app.ui.features.breeds.model.BreedUi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class BreedUiTest {

    @Test
    fun fromBreedsModel_mapsAllFields() {

        val breed = FakeBreedsModel.persian

        val result = BreedUi.fromBreedsModel(
            breed,
            false
        )

        assertEquals(breed.id, result.id)
        assertEquals(breed.name, result.name)
        assertEquals(breed.origin, result.origin)
        assertEquals(breed.temperament, result.temperament)
        assertEquals(breed.lifeSpan, result.lifeSpan)
        assertEquals(breed.description, result.description)
    }

    @Test
    fun fromBreedsModel_setsFavouriteTrue() {

        val result = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            true
        )

        assertTrue(result.isFavorite)
    }

    @Test
    fun fromBreedsModel_setsFavouriteFalse() {

        val result = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        assertFalse(result.isFavorite)
    }

    @Test
    fun fromBreedsModel_mapsImage() {

        val breed = FakeBreedsModel.persian

        val result = BreedUi.fromBreedsModel(
            breed,
            false
        )

        assertEquals(
            breed.image?.url,
            result.imageUrl.url
        )
    }

    @Test
    fun fromBreedsModel_mapsWeight() {

        val breed = FakeBreedsModel.persian

        val result = BreedUi.fromBreedsModel(
            breed,
            false
        )

        assertEquals(
            breed.weight?.metric,
            result.weight.metric
        )

        assertEquals(
            breed.weight?.imperial,
            result.weight.imperial
        )
    }


}