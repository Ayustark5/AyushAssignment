package com.ayustark.ayushassignment.utils

import com.ayustark.ayushassignment.network.responses.ApiError

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null,
    val errorData: ApiError? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, errorData: ApiError? = null) :
        Resource<T>(data, message, errorData)

    class Loading<T>(data: T? = null) : Resource<T>(data)
}