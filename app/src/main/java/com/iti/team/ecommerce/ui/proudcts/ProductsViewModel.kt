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
import kotlinx.coroutines.launch


class ProductsViewModel : ViewModel() {
    private val modelRepository: ModelRepo = ModelRepository()

    private lateinit var dataOfProduct: MutableList<Pair<Products,String>>
    private lateinit var dataOfBrand: MutableList<String>
    private val set: MutableSet<String> = HashSet()
    private val productLiveData: MutableLiveData<List<Pair<Products,String>>> = MutableLiveData()
    private val brandLiveData: MutableLiveData<List<String>> = MutableLiveData()


    fun search(name: String) {
        if (name.isNotEmpty()) {
            productLiveData.value = dataOfProduct.filter { checkItem(it.first, name) }
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


    fun getProductsData(): LiveData<List<Pair<Products,String>>> {
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
            productLiveData.value = (dataOfProduct.filter { set.contains(it.first.vendor) })
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
                   dataOfProduct = mutableListOf()
                    dataOfBrand = mutableListOf()
                    result.data?.product?.forEach {
                       it.vendor?.let { it1 ->
                           if (!dataOfBrand.contains(it1)) {
                               dataOfBrand.add(it1)
                           }
                       }
                       when (val im = modelRepository.getProductImages(it.productId!!)) {
                           is Result.Success ->{
                             dataOfProduct.add(Pair(it, im.data?.images?.get(0)?.src!!))
                               productLiveData.postValue(dataOfProduct)
                               brandLiveData.postValue(dataOfBrand)
                           }
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


}