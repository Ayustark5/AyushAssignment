package com.ayustark.ayushassignment.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ayustark.ayushassignment.R
import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.database.SwaadDatabase
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.network.ApiService
import com.ayustark.ayushassignment.network.requests.LoginRequest
import com.ayustark.ayushassignment.network.responses.ApiError
import com.ayustark.ayushassignment.network.responses.ErrorData
import com.ayustark.ayushassignment.utils.Constants
import com.ayustark.ayushassignment.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.InterruptedIOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val context: Context,
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences,
    private val swaadDatabase: SwaadDatabase
) : Repository {

    override suspend fun login(loginRequest: LoginRequest) =
        try {
            val response =
                swaadDatabase.userDao().authenticate(loginRequest.mobile, loginRequest.password)
            if (response.isNotEmpty()) {
                sharedPreferences.edit().putLong(Constants.USER_ID, response[0])
                    .putBoolean(Constants.IS_LOGGED, true).apply()
                Resource.Success(
                    response[0]
                )
            } else
                Resource.Error("No user found!! Try Again!!")
        } catch (e: Exception) {
            Log.e("LoginResponse", e.message.toString())
            Resource.Error(context.getString(R.string.some_error_occurred))
        }

    override suspend fun createUser(userEntity: UserEntity) =
        try {
            val response = swaadDatabase.userDao().createUser(userEntity)
            sharedPreferences.edit().putLong(Constants.USER_ID, response)
                .putBoolean(Constants.IS_LOGGED, true).apply()
            Resource.Success(
                response
            )
        } catch (e: Exception) {
            Log.e("RegisterResponse", e.message.toString())
            Resource.Error(context.getString(R.string.some_error_occurred))
        }

    override suspend fun getCurrentUser() =
        try {
            val userId = sharedPreferences.getLong(Constants.USER_ID, -1)
            if (userId >= 0) {
                val response = swaadDatabase.userDao().getCurrentUser(userId)
                Log.d("GetCurrentUserResponse", response.name)
                Resource.Success(
                    response
                )
            } else {
                Resource.Error("No logged-in user found $userId")
            }
        } catch (e: Exception) {
            Log.e("GetCurrentUserResponse", e.message.toString())
            Resource.Error(context.getString(R.string.some_error_occurred))
        }

    override suspend fun updateCartItem(item: CartEntity) =
        try {
            val response = if (item.quantity < 1) {
                swaadDatabase.cartDao().removeCartItem(item.id).toLong()
            } else {
                swaadDatabase.cartDao().updateCartItem(item)
            }
            Log.d("Update Cart", "$response")
            Resource.Success(
                response
            )
        } catch (e: Exception) {
            Log.e("UpdateCartResponse", e.message.toString())
            Resource.Error(context.getString(R.string.some_error_occurred))
        }

    override suspend fun getCartItems() =
        try {
            val response = swaadDatabase.cartDao().getCartItems()
            Log.d("GetCartResponse", "${response.size}")
            Resource.Success(response)
        } catch (e: Exception) {
            Log.e("GetCartResponse", e.message.toString())
            Resource.Error(context.getString(R.string.some_error_occurred), listOf())
        }

    override suspend fun emptyCart() =
        try {
            val response = swaadDatabase.cartDao().emptyCart()
            Log.d("EmptyCartResponse", "$response")
            Resource.Success(response)
        } catch (e: Exception) {
            Log.e("EmptyCartResponse", e.message.toString())
            Resource.Error("Some Error Occurred")
        }

    override suspend fun getRestaurants() = handleApiResponse {
        api.getRestaurants()
    }

    override suspend fun getMenu() = handleApiResponse {
        api.getMenu()
    }

    override suspend fun logout() =
        try {
            sharedPreferences.edit().remove(Constants.USER_ID).remove(Constants.IS_LOGGED).apply()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e("LogoutResponse", e.message.toString())
            Resource.Error("Some Error Occurred")
        }
}

suspend inline fun <reified T> handleApiResponse(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline apiCall: suspend () -> Response<T>
): Resource<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            when {
                response.isSuccessful -> {
                    Log.d("ApiResponse", "Response obtained is ${response.body().toString()}")
                    Resource.Success(response.body())
                }
                /*response.code() == HTTP_CODE_UNAUTHORIZED -> {
                    Resource.UnAuthenticated("Session Expired. Please Login")
                }*/
                else -> {
                    Log.e("ApiResponse", "Error found")
                    handleApiError(response)
                }
            }
        } catch (e: InterruptedIOException) {
            Log.e("ApiResponse", "Exception is ${e.message}", e)
            Resource.Error(
                e.message ?: "ApiError",
                null,
                ApiError(ErrorData("Request timed out.", false))
            )
        } catch (e: Exception) {
            when {
                e.message != null -> {
                    Log.e("ApiResponse", "Exception is ${e.message}", e)
                    Resource.Error(e.message ?: "ApiError", null)
                }
                else -> {
                    Log.e("ApiResponse", "An unknown error occurred")
                    Resource.Error("An unknown error occurred", null)
                }
            }
        }
    }
}

fun <T> handleApiError(response: Response<T>): Resource.Error<T> {
    val type = object : TypeToken<ApiError>() {}.type
    val reader = response.errorBody()?.charStream()
    val floApiError: ApiError = Gson().fromJson(reader, type)
    Log.d("ApiError", "Passed $floApiError")
    val formattedMessage =
        "${floApiError.data.errorMessage})."
    Log.e("ApiError", "Error $floApiError $formattedMessage")
    return Resource.Error(formattedMessage, errorData = floApiError)
}