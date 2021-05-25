package com.iti.team.ecommerce.ui.proudcts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.team.ecommerce.model.data_classes.Product

class ProductsViewModel : ViewModel() {
    var data: List<Product> = listOf(
        Product(
            "adidmmmm",
            "000", "tft", 222
        ), Product(
            "abbssas",
            "000", "tooojijijijift", 33
        ),
        Product(
            "jhuhuhu",
            "000", "tyyyyft", 5588888
        ),
        Product(
            "askjkjksas",
            "000", "lkl", 55777
        ),
        Product(
            "jjkkkkkj",
            "000", "lllll", 55777
        )
    )
    private val productLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    init {
        productLiveData.value = data
    }
    fun search(name: String) {
        if (name.isNotEmpty()) {
            productLiveData.value = (data.filter { it.name.contains(name) })
        } else {
            productLiveData.value = data
        }
    }

    fun getProductsData():LiveData<List<Product>>{
        return productLiveData
    }
    fun getBrandData():LiveData<List<Product>>{
        return productLiveData
    }


}