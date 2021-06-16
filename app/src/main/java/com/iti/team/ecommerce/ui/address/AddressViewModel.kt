package com.iti.team.ecommerce.ui.address

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.model.data_classes.AddressModel
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application)

    private var _firstName = MutableLiveData<Event<Boolean>>()
    private var _lastName = MutableLiveData<Event<Boolean>>()
    private var _city = MutableLiveData<Event<Boolean>>()
    private var _address = MutableLiveData<Event<Boolean>>()
    private var _zip = MutableLiveData<Event<Boolean>>()


    val firstName: LiveData<Event<Boolean>>
        get() = _firstName
    val lastName:LiveData<Event<Boolean>>
        get() = _lastName
    val city:LiveData<Event<Boolean>>
        get() = _city
    val address:LiveData<Event<Boolean>>
        get() = _address
    val zip:LiveData<Event<Boolean>>
        get() = _zip

    fun addAddress(customerId: Long,address: AddressModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.addAddress(customerId,address)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                    result.data?.customerAddress?.id?.let { modelRepository.setAddressID(it) }
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun getAddress(customerId: Long,addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getAddress(customerId,addressId)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun updateAddress(customerId: Long,addressId: Long,address:AddressModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.updateAddress(customerId,addressId,address)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun deleteAddress(customerId: Long,addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.deleteAddress(customerId,addressId)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun checkEmptyFields(firstName: String?,lastName: String?,
                         city: String?,address: String?,zip: String?,type:String){

        if (firstName.isNullOrBlank()){
            _firstName.postValue(Event(true))
        }

        if (lastName.isNullOrBlank()){
            _lastName.postValue(Event(true))
        }
        if (city.isNullOrBlank()){
            _city.postValue(Event(true))
        }
        if (address.isNullOrBlank()){
            _address.postValue(Event(true))
        }
        if (zip.isNullOrBlank()){
            _zip.postValue(Event(true))
        }

        if(!firstName.isNullOrBlank()&&!lastName.isNullOrBlank()&&
            !city.isNullOrBlank()&&!address.isNullOrBlank()&&
            !zip.isNullOrBlank()){
            addOrEdit(firstName,lastName,city,address,zip,type)

        }

    }

    private fun addOrEdit(firstName: String?,lastName: String?,
                                      city: String?,address: String?,zip: String?,type:String){
        val addressModel = AddressModel(Address(address,city,firstName,lastName,zip))
        if (type == Constants.EDIT){
            updateAddress(modelRepository.getCustomerID(),modelRepository.getAddressID(),addressModel)
        }else if(type == Constants.ADD){
            addAddress(modelRepository.getCustomerID(),addressModel)
        }

    }
}