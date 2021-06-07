package com.iti.team.ecommerce.ui.search

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import com.iti.team.ecommerce.utils.moshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application))


    var searchProductAdapter = SearchProductAdapter(this)
    private var _shopProductImage = MutableLiveData<String>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()

    private var _loading = MutableLiveData<Int>()
    private var _navigateToDetails = MutableLiveData<Event<String>>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()

    private var idSet: HashSet<Long> = hashSetOf()

    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails


    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    val loading: LiveData<Int>
        get() = _loading

    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                modelRepository.getAllId().collect {
                    idSet = HashSet(it)
                }
            }
        }
    }

    fun getData(product: String,type: String) {
        checkIsEqual(product,type)
    }

    private fun checkIsEqual(product: String, type: String) {
        val stringCustom = "t-shirts,shoes,accessories"
        when {
            stringCustom.contains(product.lowercase()) -> {
                Log.i("getData","contain data")
                Log.i("getData",product)
                getProductsByType(product)
            }
            type.contains(product.lowercase()) -> {
                getProductsByVendor(product)
            }
            else -> {
                Log.i("getData","not get data")
            }
        }
    }

   private fun getProductsByType(productType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromType(productType)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        searchProductAdapter.loadData(ArrayList())
                        //_loading.postValue(View.VISIBLE)
                        result.data?.product?.let {
                            searchProductAdapter.loadData(it)
                           // _loading.postValue(View.GONE)
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

    fun getProductsByVendor(vendor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromVendor(vendor)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        searchProductAdapter.loadData(ArrayList())
                        _loading.postValue(View.VISIBLE)
                        result.data?.product?.let {
                            searchProductAdapter.loadData(it)
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

    fun navigateToDetails(product: Products) {
        convertObjectToString(product)
    }

    private fun convertObjectToString(productObject: Products) {
        val adapterCurrent: JsonAdapter<Products?> = moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject))
    }


    private fun sendObjectToDetailsScreen(objectString: String) {
        _navigateToDetails.postValue(Event(objectString))
    }

    fun inWishList(id: Long): Boolean {
        return idSet.contains(id)
    }

    fun addToWishList(products: Products, image: String) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToWishList(
                Product(
                    products.productId ?: 0,
                    products.title ?: "",
                    image,
                    products.vendor ?: "",
                    (products.variants[0]?.price ?: "")
                )
            )
        }
    }

    fun removeFromWishList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeFromWishList(id)
        }

    }
}