package com.iti.team.ecommerce.ui.address

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.model.data_classes.AddressModel
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAddressViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application)
    private var  addressItem:Address? = null

    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    private var _finishLoading = MutableLiveData<Event<Boolean>>()
    private var _error = MutableLiveData<Event<Boolean>>()

    private var _firstName = MutableLiveData<Event<Boolean>>()
    private var _lastName = MutableLiveData<Event<Boolean>>()
    private var _city = MutableLiveData<Event<Boolean>>()
    private var _address = MutableLiveData<Event<Boolean>>()
    private var _zip = MutableLiveData<Event<Boolean>>()

    private var _addressObject = MutableLiveData<Event<Address>>()


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

    val addressObject:LiveData<Event<Address>>
        get() = _addressObject


    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val finishLoading: LiveData<Event<Boolean>>
        get() = _finishLoading

    val error: LiveData<Event<Boolean>>
        get() = _error

    fun addAddress(customerId: Long,address: AddressModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.addAddress(customerId,address)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                    result.data?.customerAddress?.id?.let { modelRepository.setAddressID(it) }
                    _finishLoading.postValue(Event(true))
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _error.postValue(Event(true))
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                    _error.postValue(Event(true))
                }
            }
        }
    }


    fun updateAddress(customerId: Long,addressId: Long,address:AddressModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.updateAddress(customerId,addressId,address)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customerAddress?.id}")
                    _finishLoading.postValue(Event(true))
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _error.postValue(Event(true))
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                    _error.postValue(Event(true))
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

    fun setAddress(addressObject:String){
        Log.i("setAddress","add $addressObject")
        if(addressObject != ""){
            convertStringToObject(addressObject)
        }
    }
    private fun convertStringToObject(addressObject: String){

        val productAdapter = Constants.moshi.adapter(Address::class.java)
        addressItem = productAdapter.fromJson(addressObject)

        updateProduct(addressItem)
    }

    fun updateProduct(address: Address?){
        address?.let {
         _addressObject.postValue(Event(address))
        }

    }

    fun buttonBackClicked(){
        Log.i("AddressViewModel","buttonBackClicked")
        _buttonBackClicked.postValue(Event(true))
    }


}