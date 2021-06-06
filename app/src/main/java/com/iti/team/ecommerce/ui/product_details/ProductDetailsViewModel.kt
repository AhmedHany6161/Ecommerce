package com.iti.team.ecommerce.ui.product_details

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import com.iti.team.ecommerce.utils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel: ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository(null)

    private var _descriptionText = MutableLiveData<String>()
    private var _taxable = MutableLiveData<String>()
    private var _quantity = MutableLiveData<String>()
    private var _title= MutableLiveData<String>()
    private var _price = MutableLiveData<String>()
    private var _vendor = MutableLiveData<String>()
    private var _imageProduct = MutableLiveData<List<Images>>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()

    private var _fragmentVisibility = MutableLiveData<Int>()

    val descriptionText:LiveData<String>
    get() = _descriptionText

    val taxable:LiveData<String>
    get() = _taxable

    val quantity:LiveData<String>
        get() = _quantity

    val title:LiveData<String>
        get() = _title

    val price:LiveData<String>
        get() = _price

    val vendor:LiveData<String>
        get() = _vendor

    val buttonBackClicked:LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val imageProduct:LiveData<List<Images>>
        get() = _imageProduct

    val fragmentVisibility:LiveData<Int>
        get() = _fragmentVisibility


    init {
        _fragmentVisibility.postValue(View.GONE)
    }
    fun setProduct(productObject: String){
        convertStringToObject(productObject)

    }

    private fun convertStringToObject(productObject: String){
        val productAdapter = moshi.adapter(Products::class.java)
        val product:Products? = productAdapter.fromJson(productObject)
        updateProduct(product)
    }

     fun updateProduct(products: Products?){
        products?.let {
           getProductImage(it.productId)
            Log.i("description","${it.description}" )
            it.description?.let { it1 -> _descriptionText.value = it1 }
            it.vendor?.let { it1 -> _vendor.value = it1 }
            it.title?.let { it1 -> _title.value = it1 }
            it.variants[0]?.price?.let { it1 -> _price.value = "EGP $it1" }
            it.variants[0]?.quantity?.let { it1 -> _quantity.value = it1.toString() }
            it.variants[0]?.taxable?.let { it1 -> _taxable.value = it1.toString() }

        }

    }

     private fun getProductImage(productId: Long? = 6687366217926){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result =productId?.let { modelRepository.getProductImages(it) } ){
                is Result.Success -> {
                    Log.i(
                        "getProductImage:", "${
                            result.data?.images?.let {
                                    it[0].src
                                }
                        }")
                    result.data?.images?.let {  _imageProduct.postValue(it) }
                }
                is Result.Error ->{
                    Log.e("getProductImage:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("getProductImage","Loading")}
            }
        }

    }
    fun backButtonClicked(){
        _buttonBackClicked.postValue(Event(true))
    }

    fun moreMenuClicked(){
        if (_fragmentVisibility.value == View.VISIBLE){
            _fragmentVisibility.postValue(View.GONE)
        }else{
            _fragmentVisibility.postValue(View.VISIBLE)
        }
    }
    fun layoutClicked(){
        if (_fragmentVisibility.value == View.VISIBLE){
            _fragmentVisibility.postValue(View.GONE)
        }
    }
}