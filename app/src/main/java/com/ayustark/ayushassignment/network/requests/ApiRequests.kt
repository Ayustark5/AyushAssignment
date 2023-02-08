package com.ayustark.ayushassignment.network.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginRequest(
    @SerializedName("mobile_number") val mobile: String,
    @SerializedName("password") val password: String
)