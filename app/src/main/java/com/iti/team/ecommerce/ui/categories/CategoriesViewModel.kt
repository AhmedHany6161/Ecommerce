package com.iti.team.ecommerce.ui.categories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.MainCollections
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
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashSet

class CategoriesViewModel(application: Application): AndroidViewModel(application) {

    private val _application:Application = application
    private val  modelRepository:ModelRepository = ModelRepository(
        OfflineDatabase.getInstance(application),
        application.applicationContext
    )

    private  var _mainCategories:MutableLiveData<List<MainCollections>> =MutableLiveData()
    private var _navigateToWish = MutableLiveData<Event<Boolean>>()
    private var _navigateToCart = MutableLiveData<Event<Boolean>>()
    private var _navigateToSearch = MutableLiveData<Event<String>>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()
    private var _loading = MutableLiveData<Int>()
    private var _navigateToDetails = MutableLiveData<Event<String>>()
    private var _navigateToLogin = MutableLiveData<Boolean>()
    private var _cartCount = MutableLiveData<Int>()
    private var idSet: HashSet<Long> = hashSetOf()
    val mainCategories: LiveData<List<MainCollections>>
        get() = _mainCategories
    val cartCount: LiveData<Int>
        get() = _cartCount
    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    val loading: LiveData<Int>
        get() = _loading

    val navigateToWish: LiveData<Event<Boolean>>
        get() = _navigateToWish

    val navigateToCart: LiveData<Event<Boolean>>
        get() = _navigateToCart

    val navigateToSearch: LiveData<Event<String>>
        get() = _navigateToSearch

    var categoriesProductAdapter = CategoryProductsAdapter(this)
    private var productTypeSet: HashSet<String> = hashSetOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                modelRepository.getAllId().collect {
                    idSet = HashSet(it)
                }
            }
            launch {
                modelRepository.getCartProducts().collect {
                    _cartCount.postValue(it.size)
                }
            }
        }
        productTypeSet.add("t-shirts")
        productTypeSet.add("shoes")
        productTypeSet.add("accessories")
    }

    fun getProductsById(collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProducts(collectionId)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        categoriesProductAdapter.loadData(ArrayList())
                        result.data?.product?.let {
                            categoriesProductAdapter.loadData(it)
                            _loading.postValue(View.GONE)
                        }
                    }
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loading.postValue(View.GONE)
                }
                is Result.Loading -> {
                    _loading.postValue(View.VISIBLE)
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun navigateToDetails(product: Products){
        convertObjectToString(product)
    }

    private fun convertObjectToString(productObject: Products){
        val adapterCurrent: JsonAdapter<Products?> = Constants.moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject))
    }

    private fun sendObjectToDetailsScreen(objectString: String){
        _navigateToDetails.postValue(Event(objectString))
    }
    fun navigateToWish(){
        _navigateToWish.postValue(Event(true))
    }

    fun navigateToCart(){
        _navigateToCart.postValue(Event(true))
    }

    fun navigateToSearch(){
        Log.i("navigateToSearch","${productTypeSet.size}")
        _navigateToSearch.postValue(Event(productTypeSet.toString()))
        Log.i("navigateToSearch","$productTypeSet")
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

    fun getMainCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = modelRepository.getMainCategories()){
                is Result.Success->{
                    Log.i("getCategories:", "${result.data}")
                    _mainCategories.postValue(result.data?.collections ?: listOf())
                }
                is Result.Error ->{Log.e("getCategories:", "${result.exception.message}")}
                is Result.Loading ->{Log.i("getCategories","Loading")}
            }
        }
    }

      fun getProductFromType(productType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromType(productType)) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        categoriesProductAdapter.loadData(ArrayList())

                        result.data?.product?.let {
                            categoriesProductAdapter.loadData(it)
                            _loading.postValue(View.GONE)
                        }
                    }
                }
                is Result.Error -> {
                    Log.e("getProductsFromType:", "${result.exception.message}")
                    _loading.postValue(View.GONE)
                }
                is Result.Loading -> {
                    _loading.postValue(View.VISIBLE)
                    Log.i("getProductsFromType", "Loading")
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        val connectivityManager =
            _application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    fun isLogin():Boolean{
       return modelRepository.isLogin()
    }
    fun navigateToLogin(){
        _navigateToLogin.value=true
    }
}