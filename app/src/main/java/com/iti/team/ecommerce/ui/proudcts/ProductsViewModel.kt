package com.iti.team.ecommerce.ui.proudcts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.team.ecommerce.model.data_classes.Product

class ProductsViewModel : ViewModel() {
    private var dataOfProduct: List<Product> = listOf(
        Product(
            121,
            "adidmmmm",
            "000", "tft", 222
        ),
    )
    private var dataOfBrand: List<String> = listOf(
        "tft",
        "tooojijijijift",
        "tyyyyft",
        "lkl",
        "lllll",
    )
    private val set: MutableSet<String> = HashSet()
    private val productLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    private val brandLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        productLiveData.value = dataOfProduct
        brandLiveData.value = dataOfBrand
    }

    fun search(name: String) {
        if (name.isNotEmpty()) {
            productLiveData.value = dataOfProduct.filter { set.contains(it.brand)&&it.name.contains(name) }
        } else {
            filterBrand()
        }
    }

    fun getProductsData(): LiveData<List<Product>> {
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
            productLiveData.value = (dataOfProduct.filter { set.contains(it.brand) })
        } else {
            productLiveData.value = dataOfProduct
        }
    }
    fun removeBrandFilter(name: String) {
        set.remove(name)
        filterBrand()
    }
}