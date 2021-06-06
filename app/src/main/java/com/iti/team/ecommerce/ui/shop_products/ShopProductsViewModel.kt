package com.iti.team.ecommerce.ui.shop_products

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.ui.shop.ShopAdapter
import com.iti.team.ecommerce.utils.extensions.Event
import com.iti.team.ecommerce.utils.moshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopProductsViewModel:ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository(null)
    var shopProductAdapter = ShopProductAdapter(this)
    private var _shopProductImage = MutableLiveData<String>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()

    private var _loading = MutableLiveData<Int>()
    private var _navigateToDetails = MutableLiveData<Event<String>>()

    val navigateToDetails:LiveData<Event<String>>
        get() = _navigateToDetails


    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    val loading : LiveData<Int>
        get() = _loading

    fun getData(imageUrl: String){
        _shopProductImage.postValue(imageUrl)
    }
    fun getProducts(collectionId:Long){
            viewModelScope.launch(Dispatchers.IO) {
                when (val result = modelRepository.getProducts(collectionId)) {
                    is Result.Success -> {
                        Log.i("getProducts:", "${result.data?.product}")
                        withContext(Dispatchers.Main) {
                            dataOfProduct.clear()
                            shopProductAdapter.loadData(ArrayList())
                            _loading.postValue(View.VISIBLE)
                            result.data?.product?.let {
                                shopProductAdapter.loadData(it)
                                _loading.postValue(View.GONE)
                            }
                        }
                    }

                    is Result.Error -> {
                        Log.e("getDiscount:", "${result.exception.message}")
                        _loading.postValue(View.GONE)
                    }
                    is Result.Loading -> {
                        _loading.postValue(View.GONE)
                        Log.i("getDiscount", "Loading")
                    }
                }
            }
    }

    fun navigateToDetails(product: Products){
        convertObjectToString(product)
    }

    private fun convertObjectToString(productObject: Products){
        val adapterCurrent: JsonAdapter<Products?> = moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject))
    }


    private fun sendObjectToDetailsScreen(objectString: String){
        _navigateToDetails.postValue(Event(objectString))
    }
}