package com.iti.team.ecommerce.ui.address

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.model.data_classes.AddressModel
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application)

    fun addAddress(customerId: Long,address: AddressModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.addAddress(customerId,address)) {
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
}