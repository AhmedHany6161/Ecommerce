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
import com.iti.team.ecommerce.utils.extensions.Event
import com.iti.team.ecommerce.utils.moshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application))
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()
    private var dataOfBrand: MutableList<String> = mutableListOf()
    private val filteredSet: MutableSet<String> = HashSet()
    private var productFlowData: MutableLiveData<List<Pair<Products, String>>> = MutableLiveData()
    private var brandFlowData: MutableLiveData<List<String>> = MutableLiveData()
    private val stateProductType: MutableStateFlow<String?> = MutableStateFlow(null)

    private var _navigateToDetails = MutableLiveData<Event<String>>()

    val navigateToDetails:LiveData<Event<String>>
    get() = _navigateToDetails

    private var idSet: HashSet<Long> = hashSetOf()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                stateProductType.collect {
                    if (it != null && dataOfProduct.isEmpty()) {
                        val list = getMainDataForCard(it)
                        bindBrands(list)
                        brandFlowData.postValue(dataOfBrand)
                        fetchImages(list)
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


    fun inWishList(id: Long): Boolean {
        return idSet.contains(id)
    }

    fun search(name: String) {
        if (name.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { checkIsAccepted(it.first, name) })
        } else {
            filterBrand()
        }
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

    private fun checkIsAccepted(
        it: Products,
        name: String
    ) =
        (filteredSet.contains(it.vendor) || filteredSet.isEmpty()) && it.title?.lowercase()
            ?.contains(name.lowercase()) ?: false

    fun getProductsData(): LiveData<List<Pair<Products, String>>> {
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
            productFlowData.value = (dataOfProduct.filter { filteredSet.contains(it.first.vendor) })
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

    private suspend fun fetchImages(list: List<Products>) {
        list.forEach {
            when (val im = modelRepository.getProductImages(it.productId!!)) {
                is Result.Success -> {
                    dataOfProduct.add(Pair(it, im.data?.images?.get(0)?.src!!))
                    productFlowData.postValue(dataOfProduct)
                }
            }
        }

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
     fun inFilteredList(name : String):Boolean{
        return filteredSet.contains(name)
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