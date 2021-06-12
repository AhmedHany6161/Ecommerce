package com.iti.team.ecommerce.ui.shop_products

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopProductsViewModel(application: Application) : AndroidViewModel(application) {
     val  modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application),application)


    var shopProductAdapter = ShopProductAdapter(this)
    private var _shopProductImage = MutableLiveData<String>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()

    private var _loading = MutableLiveData<Int>()
    private var _navigateToDetails = MutableLiveData<Event<Pair<String,Boolean?>>>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    private var _navigateToLogin = MutableLiveData<Event<Boolean>>()

    private var idSet: HashSet<Long> = hashSetOf()

    val navigateToDetails:LiveData<Event<Pair<String,Boolean?>>>
        get() = _navigateToDetails


    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    val loading : LiveData<Int>
        get() = _loading

    val buttonBackClicked:LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

   private fun readWishList(){
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                modelRepository.getAllId().collect {
                    idSet = HashSet(it)
                }
            }
        }
    }
        fun getData(imageUrl: String) {
            _shopProductImage.postValue(imageUrl)
        }

        fun getProducts(collectionId: Long) {
            readWishList()
            viewModelScope.launch(Dispatchers.IO) {
                when (val result = modelRepository.getProducts(collectionId)) {
                    is Result.Success -> {
                        Log.i("getProducts:", "${result.data?.product}")
                        withContext(Dispatchers.Main) {
                            dataOfProduct.clear()
                            shopProductAdapter.loadData(ArrayList())
                            _loading.postValue(View.VISIBLE)
                            result.data?.product?.let {
                                shopProductAdapter.loadData(it)
                                _loading.postValue(View.GONE)
                            }
                        }
                    }

                    is Result.Error -> {
                        Log.e("getDiscount:", "${result.exception.message}")
                        _loading.postValue(View.GONE)
                    }
                    is Result.Loading -> {
                        _loading.postValue(View.GONE)
                        Log.i("getDiscount", "Loading")
                    }
                }
            }
        }

    fun navigateToDetails(product: Products){
        convertObjectToString(product)
    }

    private fun convertObjectToString(productObject: Products){
        val inWish = productObject.productId?.let { inWishList(it) }
        val adapterCurrent: JsonAdapter<Products?> = Constants.moshi.adapter(Products::class.java)
        sendObjectToDetailsScreen(adapterCurrent.toJson(productObject),inWish)

    }


    private fun sendObjectToDetailsScreen(objectString: String,inWish:Boolean?){
        _navigateToDetails.postValue(Event(Pair(objectString,inWish)))
    }

    fun inWishList(id: Long): Boolean {
        return idSet.contains(id)
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

    fun backButtonClicked(){
        _buttonBackClicked.postValue(Event(true))
    }
    fun navToLogin(){
        _navigateToLogin.postValue(Event(true))
    }
}