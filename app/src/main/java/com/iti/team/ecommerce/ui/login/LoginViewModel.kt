package com.iti.team.ecommerce.ui.login

import android.app.Application
import android.util.Log

import androidx.lifecycle.*


import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel( application: Application): AndroidViewModel(application)  {
    private val  modelRepository: ModelRepo = ModelRepository(null,application)

    var password =""
    private val isEmptyEmail = MutableLiveData<Boolean>()
    private val isEmptyPass = MutableLiveData<Boolean>()
    private val isValid = MutableLiveData<Boolean>()


    fun isEmailEmpty(): LiveData<Boolean> = isEmptyEmail
    fun isPassEmpty(): LiveData<Boolean> = isEmptyPass
    fun isValidEmail(): LiveData<Boolean> = isValid



    fun emailResult(txt_email:String ) {

        if (txt_email.isBlank()) {
            isEmptyEmail.value = false
            return
        }
        isEmptyEmail.value = true
    }

    fun passwordResult(txt_pass:String ) {

        if (txt_pass.isBlank()) {
            isEmptyPass.value = false
            return
        }
        isEmptyPass.value = true
    }

    fun login(email: String?){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result =email?.let { modelRepository.login(email) } ){
                is Result.Success -> {
                    Log.i("login:", "${result.data}")
                    if (result.data?.customer?.isEmpty()!! || result.data.customer.size > 1){
                        withContext(Dispatchers.Main) {
                            isValid.value = false
                            Log.i("login:", "invalid email , customer =null")
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            password = result.data.customer.get(0)?.note.toString()
                            isValid.value = true
                            Log.i("login:", "valid email")
                            Log.i("login:", password)
                            modelRepository.setLogin(true)
                        }
                    }
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
            modelRepository.setLogin(true)
            modelRepository.setEmail(user.email?:"unknown")
            AuthenticationState.AUTHENTICATED
        } else {
            modelRepository.setLogin(false)
            AuthenticationState.UNAUTHENTICATED
        }
    }

}