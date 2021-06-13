package com.iti.team.ecommerce.ui.product_details

import android.app.Application
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import com.iti.team.ecommerce.utils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {
     val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application.applicationContext)

    private var  inWishL: Boolean? = false
    private var  product:Products? = null
    var sizeAdapter = SizeAdapter()
    private var _descriptionText = MutableLiveData<String>()
    private var _taxable = MutableLiveData<String>()
    private var _quantity = MutableLiveData<String>()
    private var _title= MutableLiveData<String>()
    private var _price = MutableLiveData<String>()
    private var _vendor = MutableLiveData<String>()
    private var _imageProduct = MutableLiveData<List<Images>>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()

    private var _fragmentVisibility = MutableLiveData<Int>()
    private var _inWish = MutableLiveData<Event<Boolean>>()
    private var _favoriteIconColor = MutableLiveData<Int>()
    private var _navigateToLogin = MutableLiveData<Event<Boolean>>()
    private var _addToCart = MutableLiveData<Event<Boolean>>()

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

    val inWish:LiveData<Event<Boolean>>
        get() = _inWish

    val favoriteIconColor:LiveData<Int>
        get() = _favoriteIconColor

    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

    val addToCart: LiveData<Event<Boolean>>
        get() = _addToCart

    init {
        _fragmentVisibility.postValue(View.GONE)
    }
    fun setProduct(productObject: String){
        convertStringToObject(productObject)

    }

    private fun convertStringToObject(productObject: String){
        val productAdapter = moshi.adapter(Products::class.java)
        product = productAdapter.fromJson(productObject)
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
            it.options[0]?.values?.let { it1 -> sizeAdapter.loadData(it1) }
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

    fun favoriteIconClicked(){
        if(modelRepository.isLogin()) {
            if (inWishL == true) {
                _favoriteIconColor.postValue(Color.GRAY)
                product?.productId?.let {
                    removeFromWishList(it)
                    inWishL = false
                }
            } else {
                _favoriteIconColor.postValue(Color.RED)
                product?.image?.src?.let {
                    product?.let { it1 -> addToWishList(it1, it) }
                    inWishL = true
                }

            }
        }else{
            _navigateToLogin.postValue(Event(true))
        }
    }
    fun layoutClicked(){
        if (_fragmentVisibility.value == View.VISIBLE){
            _fragmentVisibility.postValue(View.GONE)
        }
    }

    fun inWish(inWishL: Boolean) {
        this.inWishL = inWishL
        _inWish.postValue(Event(true))
        if(inWishL){
            _favoriteIconColor.postValue(Color.RED)
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

    fun addToCart(){
        if(modelRepository.isLogin()){
            viewModelScope.launch(Dispatchers.IO) {
                product?.image?.src?.let {
                    Product(
                        product?.productId ?: 0,
                        product?.title ?: "",
                        it,
                        product?.vendor ?: "",
                        (product?.variants?.get(0)?.price ?: "")
                    )
                }?.let {
                    modelRepository.addToCart(
                        it
                    )
                }
                _addToCart.postValue(Event(true))
            }

        }else{
            _navigateToLogin.postValue(Event(true))
        }
    }
}