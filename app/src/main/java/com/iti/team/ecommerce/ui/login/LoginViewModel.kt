package com.iti.team.ecommerce.ui.login


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Customer
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.*

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
                            result.data.customer[0]?.firstName?.let { modelRepository.setUserName(it) }
                            result.data.customer[0]?.email?.let { modelRepository.setEmail(it) }
                            Log.i("login:", "valid email")
                            Log.i("login:", password)
                            modelRepository.setLogin(true)

                            modelRepository.setCustomerID(
                                result.data.customer.get(0)?.customerId ?: 0
                            )
                            modelRepository.setLastName(
                                result.data.customer.get(0)?.lastName ?: "unknown"
                            )
                            modelRepository.setPhoneNum(
                                result.data.customer.get(0)?.phone ?: "unknown"
                            )
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
        user.getContentIfNotHandled()?.let {
            if (it != null) {
                modelRepository.setLogin(true)
                modelRepository.setEmail(it.email ?: "unknown")
                modelRepository.setUserName(it.displayName ?: "unknown")
                val initUser = viewModelScope.async(Dispatchers.IO) {
                    modelRepository.createCustomer(
                        CustomerModel(
                            Customer(
                                null,
                                it.email,
                                it.phoneNumber,
                                it.displayName,
                                it.displayName,
                                password = it.email,
                                passwordConfirmation = it.email
                            ),
                            null,
                        )
                    )

                }
                val log = viewModelScope.async(Dispatchers.IO) {
                    login(it.email)
                    return@async AuthenticationState.AUTHENTICATED
                }
                runBlocking {
                    initUser.await()
                    return@runBlocking log.await()
                }
            } else {
                modelRepository.setLogin(false)
                AuthenticationState.UNAUTHENTICATED
            }
        }
    }

}