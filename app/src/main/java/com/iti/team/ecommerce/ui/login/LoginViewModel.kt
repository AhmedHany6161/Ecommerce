package com.iti.team.ecommerce.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel()  {
    private val  modelRepository: ModelRepo = ModelRepository(null)

    fun login(email: String?){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result =email?.let { modelRepository.login(email) } ){
                is Result.Success -> {
                    Log.i(
                        "login:", "${
                            result.data
                        }")
                }
                is Result.Error ->{
                    Log.e("login:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("login","Loading")}
            }
        }

    }

    enum class AuthenticationState{
        AUTHENTICATED , UNAUTHENTICATED
    }
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

}