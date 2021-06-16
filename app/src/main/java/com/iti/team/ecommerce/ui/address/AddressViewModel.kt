package com.iti.team.ecommerce.ui.address

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application)
    private lateinit var address:Address

    private var _noAddressVisibility = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Int>()
    private var _cardVisibility = MutableLiveData<Int>()
    private var _cityText= MutableLiveData<String>()
    private var _addressText = MutableLiveData<String>()
    private var _zipText = MutableLiveData<String>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    private var _buttonAddAddressClicked = MutableLiveData<Event<Boolean>>()
    private var _editButtonClicked = MutableLiveData<Event<String>>()

    val noAddressVisibility : LiveData<Int>
        get() = _noAddressVisibility


    val loadingVisibility : LiveData<Int>
        get() = _loadingVisibility


    val cardVisibility : LiveData<Int>
        get() = _cardVisibility

    val cityText: LiveData<String>
        get() = _cityText
    val addressText : LiveData<String>
        get() = _addressText
    val zipText : LiveData<String>
        get() = _zipText


    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val buttonAddAddressClicked: LiveData<Event<Boolean>>
        get() = _buttonAddAddressClicked
    val editButtonClicked: LiveData<Event<String>>
        get() = _editButtonClicked

    init {
        _noAddressVisibility.postValue(View.GONE)
        _cardVisibility.postValue(View.GONE)
    }

    fun getAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getAddress(modelRepository.getCustomerID())) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.customer?.addresses}")
                    if (result.data?.customer?.addresses?.isEmpty() == true){
                        _noAddressVisibility.postValue(View.VISIBLE)
                        _loadingVisibility.postValue(View.GONE)
                    }else{
                        _loadingVisibility.postValue(View.GONE)
                        _noAddressVisibility.postValue(View.GONE)
                        _cardVisibility.postValue(View.VISIBLE)
                        _cityText.postValue(result.data?.customer?.addresses?.let {it[0].city})
                        _addressText.postValue(result.data?.customer?.addresses?.let {it[0].address})
                        _zipText.postValue(result.data?.customer?.addresses?.let {it[0].zip})
                        address = Address(result.data?.customer?.addresses?.let {it[0].address},
                            result.data?.customer?.addresses?.let {it[0].city},
                            result.data?.customer?.addresses?.let {it[0].firstName},
                            result.data?.customer?.addresses?.let {it[0].lastName},
                            result.data?.customer?.addresses?.let {it[0].zip})
                    }
                }
                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loadingVisibility.postValue(View.GONE)
                }
                is Result.Loading -> {
                    Log.i("getDiscount", "Loading")
                    _loadingVisibility.postValue(View.GONE)
                }
            }
        }
    }
    fun buttonBackClicked(){
        Log.i("AddressViewModel","buttonBackClicked")
        _buttonBackClicked.postValue(Event(true))
    }

    fun buttonAddAddressClicked(){
        Log.i("AddressViewModel","buttonAddAddressClicked")
        _buttonAddAddressClicked.postValue(Event(true))
    }

    fun editButtonClicked(){
        Log.i("AddressViewModel","editButtonClicked")
        convertObjectToString(address)

    }

    fun navigateToDetails(address: Address){
        convertObjectToString(address)
    }

    private fun convertObjectToString(addressObject: Address){
        val adapterCurrent: JsonAdapter<Address?> = Constants.moshi.adapter(Address::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(addressObject))

    }


    private fun sendObjectToDetailsScreen(objectString: String){
        _editButtonClicked.postValue(Event(objectString))
    }
}