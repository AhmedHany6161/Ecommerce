package com.iti.team.ecommerce.ui.login

import android.util.Log
import androidx.lifecycle.*

import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel: ViewModel()  {
    private val  modelRepository: ModelRepo = ModelRepository(null)

    var password =""
    private val emptyResult = MutableLiveData<String>()
    private val validate = MutableLiveData<String>()
    private val isValid = MutableLiveData<String>("null")

    fun getLogInResult(): LiveData<String> = emptyResult
    fun getvalidation(): LiveData<String> = validate
    fun getLogInState(): LiveData<String> = isValid



    fun validationEmpty(txt_val:String ) {

        if (txt_val.isBlank()) {
            emptyResult.value = "empty"
            return
        }
        emptyResult.value = "not empty"
    }

    fun validationPassword(txt_val:String ) {

        if (txt_val.isBlank()) {
            validate.value = "empty"
            return
        }
        validate.value = "not empty"
    }

    fun login(email: String?){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result =email?.let { modelRepository.login(email) } ){
                is Result.Success -> {
                    Log.i("login:", "${result.data}")
                    if (result.data?.customer?.isEmpty()!! || result.data.customer.size > 1){
                        withContext(Dispatchers.Main) {
                            isValid.value = "false"
                            Log.i("login:", "invalid email , customer =null")
                        }
                    }else{
                        Log.i("login:", "valid email")
                        withContext(Dispatchers.Main) {
                            isValid.value ="true"
                            password = result.data.customer.get(0)?.note.toString()
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
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

}