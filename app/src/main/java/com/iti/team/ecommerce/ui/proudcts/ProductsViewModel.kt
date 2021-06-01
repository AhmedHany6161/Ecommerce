package com.iti.team.ecommerce.ui.proudcts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductsViewModel : ViewModel() {
    private val modelRepository: ModelRepo = ModelRepository(null)
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()
    private var dataOfBrand: MutableList<String> = mutableListOf()
    private val set: MutableSet<String> = HashSet()
    private var productFlowData: MutableLiveData<List<Pair<Products, String>>> = MutableLiveData()
    private var brandFlowData: MutableLiveData<List<String>> = MutableLiveData()
    private val stateProductType: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            stateProductType.collectLatest {
                if (it != null && dataOfProduct.isEmpty()) {
                    val list = getMainDataForCard(it)
                    bindBrands(list)
                    brandFlowData.postValue(dataOfBrand)
                    fetchImages(list)
                }
            }
        }
    }

    fun search(name: String) {
        if (name.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { checkIsAccepted(it.first, name) })
        } else {
            filterBrand()
        }
    }


    private fun checkIsAccepted(
        it: Products,
        name: String
    ) =
        (set.contains(it.vendor) || set.isEmpty()) && it.title?.lowercase()
            ?.contains(name.lowercase()) ?: false

    fun getProductsData(): LiveData<List<Pair<Products, String>>> {
        return productFlowData
    }

    fun getBrandData(): LiveData<List<String>> {
        return brandFlowData
    }

    fun addBrandFilter(name: String) {
        set.add(name)
        filterBrand()
    }
    private fun filterBrand(){
        if (set.isNotEmpty()) {
            productFlowData.value = (dataOfProduct.filter { set.contains(it.first.vendor) })
        } else {
            productFlowData.value = (dataOfProduct)
        }
    }

    fun removeBrandFilter(name: String) {
        set.remove(name)
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


}