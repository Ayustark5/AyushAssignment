package com.ayustark.ayushassignment.repositories

import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.network.requests.LoginRequest
import com.ayustark.ayushassignment.network.responses.MenuResponse
import com.ayustark.ayushassignment.network.responses.RestaurantResponse
import com.ayustark.ayushassignment.utils.Resource

interface Repository {

    suspend fun login(loginRequest: LoginRequest): Resource<Long>

    suspend fun createUser(userEntity: UserEntity): Resource<Long>

    suspend fun getCurrentUser(): Resource<UserEntity>

    suspend fun getRestaurants(): Resource<RestaurantResponse>

    suspend fun getMenu(): Resource<MenuResponse>

    suspend fun updateCartItem(item: CartEntity): Resource<Long>

    suspend fun getCartItems(): Resource<List<CartEntity>>

    suspend fun emptyCart(): Resource<Int>

    suspend fun logout(): Resource<Boolean>

}