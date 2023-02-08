package com.ayustark.ayushassignment.network

import com.ayustark.ayushassignment.network.requests.LoginRequest
import com.ayustark.ayushassignment.network.responses.LoginResponse
import com.ayustark.ayushassignment.network.responses.MenuResponse
import com.ayustark.ayushassignment.network.responses.RestaurantResponse
import com.ayustark.ayushassignment.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/anx/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("restaurants/fetch_result/")
    suspend fun getRestaurants(@Header("token") token: String = Constants.API_KEY): Response<RestaurantResponse>

    @GET("restaurants/fetch_result/3")
    suspend fun getMenu(@Header("token") token: String = Constants.API_KEY): Response<MenuResponse>

}