package com.iti.team.ecommerce.ui.order_details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Order
import com.iti.team.ecommerce.model.data_classes.OrderDetails
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application.applicationContext)
    private val orderDetailsList: MutableList<Pair<OrderDetails, String>> = mutableListOf()
    private val _orderDetails: MutableLiveData<List<Pair<OrderDetails, String>>> = MutableLiveData()
    val orderDetails: LiveData<List<Pair<OrderDetails, String>>> get() = _orderDetails

    fun getImages(list: List<OrderDetails>) {
        viewModelScope.launch(Dispatchers.IO) {
            list.forEach {
                when (val result = modelRepository.getProductImages(it.id)) {
                    is Result.Success -> {
                        result.data?.images?.get(0)?.src?.let { it1 ->
                            orderDetailsList.add(Pair(it, it1))
                            _orderDetails.postValue(orderDetailsList)
                        }
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

    }

    fun setOrder(order: String): List<OrderDetails> {
        return convertStringToObject(order)
    }

    private fun convertStringToObject(order: String): List<OrderDetails> {
        val productAdapter = Constants.moshi.adapter(Order::class.java)
        return productAdapter.fromJson(order)?.items ?: listOf()
    }
}