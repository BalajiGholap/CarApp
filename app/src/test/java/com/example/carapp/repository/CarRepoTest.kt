package com.example.carapp.repository

import com.example.carapp.model.Cars
import com.example.carapp.retrofit.CarApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CarRepoTest {

    @Mock
    lateinit var carApi: CarApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetCars_Empty() = runTest{
        Mockito.`when`(carApi.getCars()).thenReturn(Response.success(emptyList()))
        //val sut = CarRepo(carApi)
        //val result = sut.getCars()
        val result = carApi.getCars()
        Assert.assertEquals(true, result.body()!!.isEmpty())
        Assert.assertEquals(0, result.body()!!.size)
    }

    @Test
    fun testGetCarsList() = runTest{
        val carList = listOf<Cars>(
            Cars("BMW","1","Top","Top",3333333),
            Cars("Mercedese","1","base","Top",555555),
            Cars("Ferrary","1","top","Top",777777)
        )
        Mockito.`when`(carApi.getCars()).thenReturn(Response.success(carList))
        val result = carApi.getCars()
        Assert.assertEquals(true, result.body()!!.isNotEmpty())
        Assert.assertEquals(3, result.body()!!.size)
        Assert.assertEquals("BMW", result.body()!!.get(0).carname)
    }

    @Test
    fun testGetCars_ExpError() = runTest{
        Mockito.`when`(carApi.getCars()).thenReturn(Response.error(401,"Unauthorised".toResponseBody()))
        val result = carApi.getCars()
        Assert.assertEquals(false, result.isSuccessful)
    }

}