package com.ayustark.ayushassignment.network.responses

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class LoginResponse(
    @SerializedName("data")
    val `data`: LoginData
)

@Keep
data class LoginData(
    @SerializedName("data")
    val `data`: LoginDataX,
    @SerializedName("success")
    val success: Boolean
)

@Keep
data class LoginDataX(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("mobile_number")
    val mobileNumber: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("user_id")
    val userId: String
)

@Keep
data class RestaurantResponse(
    @SerializedName("data")
    val `data`: RestaurantData
)

@Keep
data class RestaurantData(
    @SerializedName("data")
    val `data`: List<RestaurantDataX>,
    @SerializedName("success")
    val success: Boolean
)

@Keep
data class RestaurantDataX(
    @SerializedName("cost_for_one")
    val costForOne: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: String
)

@Keep
data class MenuResponse(
    @SerializedName("data")
    val `data`: MenuData
)

@Keep
data class MenuData(
    @SerializedName("data")
    val `data`: List<MenuDataX>,
    @SerializedName("success")
    val success: Boolean
)

@Keep
data class MenuDataX(
    @SerializedName("cost_for_one")
    val costForOne: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("restaurant_id")
    val restaurantId: String,
    var quantity: Int = 0
)

@Keep
data class ApiError(
    @SerializedName("data")
    val `data`: ErrorData
)

@Keep
data class ErrorData(
    @SerializedName("errorMessage")
    val errorMessage: String,
    @SerializedName("success")
    val success: Boolean
)