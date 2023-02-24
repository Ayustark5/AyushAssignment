package com.ayustark.ayushassignment.ui.authenticate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.network.requests.LoginRequest
import com.ayustark.ayushassignment.repositories.Repository
import com.ayustark.ayushassignment.utils.Event
import com.ayustark.ayushassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticateViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _login = MutableLiveData<Event<Resource<Long>>>()
    val login: LiveData<Event<Resource<Long>>> = _login

    private val _signUp = MutableLiveData<Event<Resource<Long>>>()
    val signUp: LiveData<Event<Resource<Long>>> = _signUp

    fun sendLoginRequest(loginRequest: LoginRequest) {
        _login.value = Event(Resource.Loading())
        viewModelScope.launch {
            _login.value = Event(repository.login(loginRequest))
        }
    }

    fun createNewUser(userEntity: UserEntity) {
        _signUp.value = Event(Resource.Loading())
        viewModelScope.launch {
            _signUp.value = Event(repository.createUser(userEntity))
        }
    }

}