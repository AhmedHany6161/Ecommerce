package com.iti.team.ecommerce.ui.proudcts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductsViewModel : ViewModel() {
    private val modelRepository: ModelRepo = ModelRepository()

    private lateinit var dataOfProduct: List<Products>
    private lateinit var dataOfBrand: MutableList<String>
    private val set: MutableSet<String> = HashSet()
    private val productLiveData: MutableLiveData<List<Products>> = MutableLiveData()
    private val brandLiveData: MutableLiveData<List<String>> = MutableLiveData()


    fun search(name: String) {
        if (name.isNotEmpty()) {
            productLiveData.value = dataOfProduct.filter { checkItem(it, name) }
        } else {
            filterBrand()
        }
    }

    private fun checkItem(
        it: Products,
        name: String
    ) =
        (set.contains(it.vendor) || set.isEmpty()) && it.title?.lowercase()
            ?.contains(name.lowercase()) ?: false


    fun getProductsData(): LiveData<List<Products>> {
        return productLiveData
    }

    fun getBrandData(): LiveData<List<String>> {
        return brandLiveData
    }

    fun addBrandFilter(name: String) {
        set.add(name)
        filterBrand()
    }
    private fun filterBrand(){
        if (set.isNotEmpty()) {
            productLiveData.value = (dataOfProduct.filter { set.contains(it.vendor) })
        } else {
            productLiveData.value = dataOfProduct
        }
    }

    fun removeBrandFilter(name: String) {
        set.remove(name)
        filterBrand()
    }

     fun getProductsFromType(productType: String) {

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromType(productType)) {
                is Result.Success -> {
                   dataOfProduct = result.data?.product ?: listOf()
                   productLiveData.postValue(dataOfProduct)
                    dataOfBrand = mutableListOf()
                   dataOfProduct.forEach {
                       it.vendor?.let { it1 -> if(!dataOfBrand.contains(it1)){dataOfBrand.add(it1)} }
                   }
                   brandLiveData.postValue(dataOfBrand)
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


}