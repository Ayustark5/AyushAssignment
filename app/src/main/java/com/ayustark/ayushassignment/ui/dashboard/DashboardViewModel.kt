package com.ayustark.ayushassignment.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.network.responses.MenuResponse
import com.ayustark.ayushassignment.network.responses.RestaurantResponse
import com.ayustark.ayushassignment.repositories.Repository
import com.ayustark.ayushassignment.utils.Event
import com.ayustark.ayushassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _restaurant = MutableLiveData<Event<Resource<RestaurantResponse>>>()
    val restaurant: LiveData<Event<Resource<RestaurantResponse>>> = _restaurant

    private val _menu = MutableLiveData<Event<Resource<MenuResponse>>>()
    val menu: LiveData<Event<Resource<MenuResponse>>> = _menu

    private val _cart = MutableLiveData<Event<Resource<List<CartEntity>>>>()
    val cart: LiveData<Event<Resource<List<CartEntity>>>> = _cart

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    private val _user = MutableLiveData<Event<Resource<UserEntity>>>()
    val user: LiveData<Event<Resource<UserEntity>>> = _user

    fun getRestaurantList() {
        _restaurant.value = Event(Resource.Loading())
        viewModelScope.launch {
            _restaurant.value = Event(repository.getRestaurants())
        }
    }

    fun getCartItems() {
        _cart.value = Event(Resource.Loading())
        viewModelScope.launch {
            _cart.value = Event(repository.getCartItems())
        }
    }

    fun getMenu() {
        _menu.value = Event(Resource.Loading())
        viewModelScope.launch {
            _menu.value = Event(repository.getMenu())
        }
    }

    fun updateCartItem(item: CartEntity) {
        viewModelScope.launch {
            repository.updateCartItem(item)
        }
    }

    fun updateTotalPrice(cartList: ArrayList<CartEntity>) {
        Log.d("GetCartResponse 4", "${cartList.size}")
        var total = 0
        cartList.forEach {
            total += it.costForOne.toInt() * it.quantity
        }
        _total.value = total
        Log.d("Total Price: ", total.toString())
    }

    fun emptyCart() {
        viewModelScope.launch {
            repository.emptyCart()
        }
    }

    fun getUserDetails() {
        _user.value = Event(Resource.Loading())
        viewModelScope.launch {
            _user.value = Event(repository.getCurrentUser())
        }
    }

    fun logout() {
        emptyCart()
        viewModelScope.launch {
            repository.logout()
        }
    }

}