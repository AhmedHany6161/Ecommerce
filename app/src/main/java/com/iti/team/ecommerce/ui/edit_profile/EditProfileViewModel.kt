package com.iti.team.ecommerce.ui.edit_profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.data_classes.EditCustomerModel
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val modelRepository: ModelRepo = ModelRepository(null, application)

    var first_name = MutableLiveData<String?>()
    var last_name = MutableLiveData<String?>()
    var email = MutableLiveData<String?>()
    var phone_number = MutableLiveData<String?>()
    var full_name = MutableLiveData<String?>()
    var customer_ID = MutableLiveData<Long?>()

    private var _setError = MutableLiveData<String>()
    private var _success = MutableLiveData<Boolean>()

    val setError: LiveData<String>
        get() = _setError

    val success: LiveData<Boolean>
        get() = _success

    fun getData(){
        first_name.setValue(modelRepository.getFirstName())
        last_name.setValue(modelRepository.getLastName())
        email.setValue(modelRepository.getEmail())
        phone_number.setValue(modelRepository.getPhoneNum())
        full_name.setValue(modelRepository.getFirstName() +" "+ modelRepository.getLastName())
        customer_ID.setValue(modelRepository.getCustomerID())

    }

    fun updateCustomer(customer_id : Long ,customer: EditCustomerModel){
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = modelRepository.updateCustomer(customer_id , customer)){
                is Result.Success->{
                    Log.i("updateCustomer:", "${result.data}")
                    modelRepository.setCustomerID(result.data?.customer?.customerId?: 0)
                    modelRepository.setEmail(result.data?.customer?.email?:"unknown")
                    modelRepository.setFirstName(result.data?.customer?.firstName?:"unknown")
                    modelRepository.setLastName(result.data?.customer?.lastName?:"unknown")
                    modelRepository.setPhoneNum(result.data?.customer?.phone?:"unknown")
                    _success.postValue(true)

                }
                is Result.Error ->{
                    Log.e("updateCustomer:", "${result.exception.message}")
                    _setError.postValue(result.exception.message.toString())
                    println(result.exception.message.toString())
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

                }
                is Result.Loading ->{
                    Log.i("updateCustomer:", "${result}")
                    Log.i("updateCustomer","Loading")}
            }
        }

    }

}