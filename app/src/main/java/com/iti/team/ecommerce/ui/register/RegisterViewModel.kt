package com.iti.team.ecommerce.ui.register

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val modelRepository: ModelRepo =
        ModelRepository(null, application.applicationContext)
    private var _setError = MutableLiveData<String>()
    private var _successRegister = MutableLiveData<Boolean>()
    private var _loading = MutableLiveData<Int>(View.GONE)

    val loading: LiveData<Int>
        get() = _loading

    val successRegister: LiveData<Boolean>
        get() = _successRegister

    val setError: LiveData<String>
        get() = _setError

    fun createCustomer(customer: CustomerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.createCustomer(customer)) {
                is Result.Success -> {
                    Log.i("createCustomer:", "${result.data}")
                    _successRegister.postValue(true)
                    _loading.postValue(View.GONE)

                }
                is Result.Error -> {
                    Log.e("createCustomer:", "${result.exception.message}")
                    val jsonParser = JsonParser.parseString(result.exception.message) as JsonObject
                    val error = jsonParser.getAsJsonObject("errors")
                    var jsonArray:JsonArray? = error.getAsJsonArray("email")

                    val sb = StringBuilder()
                    if(jsonArray != null){
                        sb.append("email : ").append(jsonArray[0].asString).append("\n")
                    }
                     jsonArray = error.getAsJsonArray("phone")
                    if(jsonArray !=null){
                        sb.append("phone : ").append(jsonArray[0].asString).append("\n")
                    }
                    _setError.postValue(sb.toString())
                    _loading.postValue(View.GONE)
                }
                is Result.Loading -> {
                    Log.i("createCustomer", "Loading")
                    _loading.postValue(View.VISIBLE)
                }
            }
        }

    }
}