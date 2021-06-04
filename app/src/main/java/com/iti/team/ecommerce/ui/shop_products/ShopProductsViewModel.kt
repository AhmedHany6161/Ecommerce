package com.iti.team.ecommerce.ui.shop_products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopProductsViewModel:ViewModel() {

    private var _shopProductImage = MutableLiveData<String>()

    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    fun getData(){
        _shopProductImage.postValue("https://cdn.shopify.com/s/files/1/0567/9310/4582/collections/97a3b1227876bf099d279fd38290e567.jpg?v=1621288287")
    }
}