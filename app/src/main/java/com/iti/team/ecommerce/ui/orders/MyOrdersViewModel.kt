package com.iti.team.ecommerce.ui.orders

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.RequestAllOrdersQuery
import com.iti.team.ecommerce.model.data_classes.Order
import com.iti.team.ecommerce.model.data_classes.OrderDetails
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyOrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)
    private val _mutableOrder: MutableLiveData<List<RequestAllOrdersQuery.Edge>> = MutableLiveData()
    val orders: MutableLiveData<List<RequestAllOrdersQuery.Edge>> get() = _mutableOrder
    private lateinit var data: List<RequestAllOrdersQuery.Edge>
    private var _navigateToDetails = MutableLiveData<Event<String>>()
    private var lastState ="all"
    val navigateToDetails: LiveData<Event<String>>get() = _navigateToDetails
    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getAllOrderFormGQL(modelRepository.getEmail())) {
                is Result.Success -> {
                    data = result.data.edges
                    filterByStatus(lastState)
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

    fun filterByStatus(status: String = lastState) {
        lastState = status
        if (lastState == "unpaid"){
            lastState = "voided"
        }
        if (status != "all") {
            orders.postValue(data.filter {
                Log.e("aaaaaaaaaaaa",it.node.displayFinancialStatus?.name.toString())
                it.node.displayFinancialStatus?.name.toString().lowercase() == lastState.lowercase()
            })
        } else {
            orders.postValue(data)
        }
    }
    fun navigateToDetails(items: RequestAllOrdersQuery.LineItems) {
        val list:MutableList<OrderDetails> = mutableListOf()
        items.edges.forEach {
            list.add(OrderDetails(it.node.currentQuantity.toLong(),it.node.title,it.node.originalTotal.toString(),it.node.vendor?:"unknown",0,it.node.image?.src.toString()))
        }

        convertObjectToString(Order(items = list))
    }
    private fun convertObjectToString(order: Order){
        val adapterCurrent: JsonAdapter<Order?> = Constants.moshi.adapter(Order::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(order))

    }


    private fun sendObjectToDetailsScreen(objectString: String){
        Log.e("aaaaaaaaaaaa",objectString)
        _navigateToDetails.postValue(Event(objectString))
    }
}