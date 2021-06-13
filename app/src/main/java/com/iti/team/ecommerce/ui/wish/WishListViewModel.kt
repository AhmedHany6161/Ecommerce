package com.iti.team.ecommerce.ui.wish

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WishListViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)
    private var dataOfProduct: List<Product> = mutableListOf()
    private val productFlowData: MutableLiveData<List<Product>> by lazy { MutableLiveData() }
    private var _navigateToDetails = MutableLiveData<Event<Pair<String, Boolean?>>>()

    val navigateToDetails: LiveData<Event<Pair<String, Boolean?>>>
        get() = _navigateToDetails

    private val _addToCart = MutableLiveData<String>()
    val addToCart: LiveData<String> get() = _addToCart

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

    fun getWishLis(): LiveData<List<Product>> {
        return productFlowData
    }

    fun navigateToDetails(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromVendor(product.brand)) {
                is Result.Success -> {
                    result.data?.product?.forEach {
                        if (it.productId == product.id) {
                            convertObjectToString(it)
                            return@launch
                        }
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

    fun addToCart(product: Product) {
        _addToCart.value = "product successfully added to cart"
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToCart(product)
        }
    }

    private fun convertObjectToString(productObject: Products) {
        val adapterCurrent: JsonAdapter<Products?> = Constants.moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject))
    }

    fun getLogInState(): Boolean {
        return modelRepository.isLogin()
    }

    private fun sendObjectToDetailsScreen(objectString: String) {
        _navigateToDetails.postValue(Event(Pair(objectString, true)))
    }
}