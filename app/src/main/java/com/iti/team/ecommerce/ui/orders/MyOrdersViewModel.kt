package com.iti.team.ecommerce.ui.orders

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Order
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyOrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)
    private val _mutableOrder: MutableLiveData<List<Order?>> = MutableLiveData()
    val orders: MutableLiveData<List<Order?>> get() = _mutableOrder
    private lateinit var data: List<Order?>
    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getOrders("fayzaahmed978@gmail.com")) {
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
        if (status != "all") {
            orders.postValue(data.filter {
                it?.financialStatus == status
            })
        } else {
            orders.postValue(data)
        }
    }


}