package com.iti.team.ecommerce.ui.orders

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Order
import com.iti.team.ecommerce.model.data_classes.OrderDetails
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyOrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)
    private val _mutableOrder: MutableLiveData<List<Order?>> = MutableLiveData()
    val orders: MutableLiveData<List<Order?>> get() = _mutableOrder
    private lateinit var data: List<Order?>
    private var _navigateToDetails = MutableLiveData<Event<String>>()

    val navigateToDetails: LiveData<Event<String>>get() = _navigateToDetails
    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getOrders(modelRepository.getEmail())) {
                is Result.Success -> {
                    data = result.data?.order ?: listOf()
                    orders.postValue(data)
                }
                is Result.Error -> {
                    Log.e("getProductsFromType:", "${result.exception.message}")
                }
                is Result.Loading -> {
                    Log.i("getProductsFromType", "Loading")
                }
            }
        }
    }

    fun filterByStatus(status: String) {
        var state = status
        if (state == "unpaid"){
            state = "voided"
        }
        if (status != "all") {
            orders.postValue(data.filter {
                it?.financialStatus == state
            })
        } else {
            orders.postValue(data)
        }
    }
    fun navigateToDetails(order: Order) {
        convertObjectToString(order)
    }
    private fun convertObjectToString(order: Order){
        val adapterCurrent: JsonAdapter<Order?> = Constants.moshi.adapter(Order::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(order))

    }


    private fun sendObjectToDetailsScreen(objectString: String){
        _navigateToDetails.postValue(Event(objectString))

    }
}