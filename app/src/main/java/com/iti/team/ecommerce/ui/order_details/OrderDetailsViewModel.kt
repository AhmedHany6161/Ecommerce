package com.iti.team.ecommerce.ui.order_details


import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Order
import com.iti.team.ecommerce.model.data_classes.OrderDetails

import com.iti.team.ecommerce.utils.Constants

class OrderDetailsViewModel() : ViewModel() {

    private var orderDetailsList: List<OrderDetails> = listOf()
    private val _orderDetails: MutableLiveData<List<OrderDetails>> = MutableLiveData()
    val orderDetails: LiveData<List<OrderDetails>> get() = _orderDetails

    fun setData(list: List<OrderDetails>) {
        orderDetailsList = list
        _orderDetails.value = orderDetailsList
    }

    fun setOrder(order: String): List<OrderDetails> {
        return convertStringToObject(order)
    }

    private fun convertStringToObject(order: String): List<OrderDetails> {
        val productAdapter = Constants.moshi.adapter(Order::class.java)
        return productAdapter.fromJson(order)?.items ?: listOf()
    }
}