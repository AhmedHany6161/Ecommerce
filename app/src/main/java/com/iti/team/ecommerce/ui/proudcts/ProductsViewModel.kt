package com.iti.team.ecommerce.ui.proudcts

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application.applicationContext)
    private var dataOfProduct: MutableList<Products> = mutableListOf()
    private var dataOfBrand: MutableList<String> = mutableListOf()
    private val filteredSet: MutableSet<String> = HashSet()
    private var productFlowData: MutableLiveData<List<Products>> = MutableLiveData()
    private var brandFlowData: MutableLiveData<List<String>> = MutableLiveData()
    private val stateProductType: MutableStateFlow<String?> = MutableStateFlow(null)
    private var praType = ""

    private val _navigateToLogin:MutableLiveData<Boolean> = MutableLiveData()
     val navigateToLogin:LiveData<Boolean> get() = _navigateToLogin

    private var _navigateToDetails = MutableLiveData<Event<Pair<String,Boolean?>>>()

    val navigateToDetails:LiveData<Event<Pair<String,Boolean?>>>
    get() = _navigateToDetails

    private val _addToCart = MutableLiveData<String>()
    val addToCart: LiveData<String> get() = _addToCart


    private var idSet: HashSet<Long> = hashSetOf()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                stateProductType.collect {
                    if (it != null && (dataOfProduct.isEmpty() || praType != it)) {
                        praType = it
                        val list = getMainDataForCard(it)
                        bindBrands(list)
                        dataOfProduct.addAll(list);
                        productFlowData.postValue(dataOfProduct)
                        brandFlowData.postValue(dataOfBrand)
                    }
                }
            }
            launch {
                modelRepository.getAllId().collect {
                    idSet = HashSet(it)
                }
            }
        }
    }

    fun getLogInState(): Boolean {
        return modelRepository.isLogin()
    }

    fun isLogin(): Boolean = modelRepository.isLogin()

     fun navigateToLogin(){
       _navigateToLogin.value = true
       _navigateToLogin.value = false
     }
    fun inWishList(id: Long): Boolean {
        return idSet.contains(id)
    }

    fun search(name: String) {
        if (name.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { checkIsAccepted(it, name) })
        } else {
            filterBrand()
        }
    }

    fun addToWishList(products: Products) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToWishList(
                Product(
                    products.productId ?: 0,
                    products.title ?: "",
                    products.image.src?:"",
                    products.vendor ?: "",
                    (products.variants[0]?.price ?: ""),
                    variant_id = products.variants[0]?.id ?:0
                )
            )
        }
    }

    fun removeFromWishList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeFromWishList(id)
        }

    }

    private fun checkIsAccepted(
        it: Products,
        name: String
    ) =
        (filteredSet.contains(it.vendor) || filteredSet.isEmpty()) && it.title?.lowercase()
            ?.contains(name.lowercase()) ?: false

    fun getProductsData(): LiveData<List<Products>> {
        return productFlowData
    }

    fun getBrandData(): LiveData<List<String>> {
        return brandFlowData
    }

    fun addBrandFilter(name: String) {
        filteredSet.add(name)
        filterBrand()
    }
    private fun filterBrand(){
        if (filteredSet.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { filteredSet.contains(it.vendor) })
        } else {
            productFlowData.value = (dataOfProduct)
        }
    }

    fun removeBrandFilter(name: String) {
        filteredSet.remove(name)
        filterBrand()
    }

    fun getProductsFromType(productType: String) {
        stateProductType.value = productType
    }



    private suspend fun getMainDataForCard(productType: String): List<Products> {
        when (val result = modelRepository.getProductsFromType(productType)) {
            is Result.Success -> {
                dataOfProduct = mutableListOf()
                dataOfBrand = mutableListOf()
                return result.data?.product ?: listOf()
            }
            is Result.Error -> {
                Log.e("getProductsFromType:", "${result.exception.message}")
            }
            is Result.Loading -> {
                Log.i("getProductsFromType", "Loading")
            }
        }
        return listOf()
    }


    private fun bindBrands(list: List<Products>) {
        list.forEach {
            it.vendor?.let { it1 ->
                if (!dataOfBrand.contains(it1)) {
                    dataOfBrand.add(it1)
                }
            }
        }
    }

    fun inFilteredList(name: String): Boolean {
        return filteredSet.contains(name)
    }

    fun navigateToDetails(product: Products) {
        convertObjectToString(product)
    }

    fun addToCart(products: Products) {
        _addToCart.value = "product successfully added to cart"
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToCart(
                Product(
                    products.productId ?: 0,
                    products.title ?: "",
                    products.image.src?:"",
                    products.vendor ?: "",
                    (products.variants[0]?.price ?: ""),
                    variant_id = products.variants[0]?.id ?:0
                )
            )
        }
    }

    private fun convertObjectToString(productObject: Products){
        val inWish = productObject.productId?.let { inWishList(it) }
        val adapterCurrent: JsonAdapter<Products?> = Constants.moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject),inWish)

    }


    private fun sendObjectToDetailsScreen(objectString: String,inWish:Boolean?){
        _navigateToDetails.postValue(Event(Pair(objectString,inWish)))

    }
}