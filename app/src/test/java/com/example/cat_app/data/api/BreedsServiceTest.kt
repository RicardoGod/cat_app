package com.example.cat_app.data.api

import com.example.cat_app.data.models.BreedsModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class BreedsServiceTest {

    val myMock: IBreedsService = mock()

    @Before
    fun setUp() {

    }

    @Test
    fun getBreedsList() {

        whenever { myMock.getBreedsList(0, 10) }.thenReturn(Response.success(listOf(aBreedModel())));


    }

    private fun aBreedModel(): BreedsModel{
        return BreedsModel("1", "Test", "Mock", "cold", "a description", "short", "refId", null, null)
    }

}