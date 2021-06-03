package com.iti.team.ecommerce.ui.wish

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WishListViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application))
    private var dataOfProduct: List<Product> = mutableListOf()
    private val productFlowData: MutableLiveData<List<Product>> by lazy { MutableLiveData() }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.getAllWishListProducts().collect {
                dataOfProduct = it
                productFlowData.postValue(dataOfProduct)
            }
        }
    }


    fun search(name: String) {
        if (name.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { checkIsAccepted(it, name) })
        } else {
            productFlowData.value = dataOfProduct
        }
    }


    fun removeFromWishList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeFromWishList(id)
        }

    }

    private fun checkIsAccepted(
        it: Product,
        name: String
    ) =
        (it.name.lowercase().contains(name.lowercase()))
    fun getWishLis():LiveData<List<Product>>{
        return productFlowData
    }
}