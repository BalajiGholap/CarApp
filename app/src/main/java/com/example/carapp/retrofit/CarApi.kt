package com.example.carapp.retrofit

import com.example.carapp.model.Cars
import com.example.carapp.model.Products
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarApi {

    //@GET("products")
    //suspend fun getProducts(): Response<List<Products>>

    @POST("cars")
    suspend fun addCar(@Body cars: Cars): Response<Cars>

    @GET("cars")
    suspend fun getCars(): Response<List<Cars>>

    @PUT("cars/{id}")
    suspend fun updateCar(@Path("id") id: Int, @Body cars: Cars): Response<Cars>

    @PATCH("cars/{id}")
    suspend fun patchCar(@Path("id") id: Int, @Body cars: Cars): Response<Cars>

    @DELETE("cars/{id}")
    suspend fun deleteCar(@Path("id") id: Int): Response<Cars>
}