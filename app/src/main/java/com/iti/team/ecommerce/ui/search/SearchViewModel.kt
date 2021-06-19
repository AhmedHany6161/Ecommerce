package com.iti.team.ecommerce.ui.search

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.model.reposatory.OfflineRepo
import com.iti.team.ecommerce.ui.shop.ShopViewModel
import com.iti.team.ecommerce.utils.Constants
import com.iti.team.ecommerce.utils.extensions.Event

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(val modelRepository: ModelRepo,
                      val offlineRepository: OfflineRepo
) : ViewModel(){
//    private val modelRepository: ModelRepository =
//        ModelRepository(OfflineDatabase.getInstance(application),application.applicationContext)


    var searchProductAdapter = SearchProductAdapter(this)
    private var _shopProductImage = MutableLiveData<String>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()

    private var _loading = MutableLiveData<Int>()
    private var _navigateToDetails = MutableLiveData<Event<Pair<String,Boolean?>>>()
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()

    private var _animationVisibility = MutableLiveData<Int>()
    private var _recyclerVisibility = MutableLiveData<Int>()
    private var _navigateToLogin = MutableLiveData<Event<Boolean>>()

    private var _searchText = MutableLiveData<String>()

    private var idSet: HashSet<Long> = hashSetOf()

    val navigateToDetails: LiveData<Event<Pair<String,Boolean?>>>
        get() = _navigateToDetails



    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    val loading: LiveData<Int>
        get() = _loading

    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val animationVisibility: LiveData<Int>
        get() = _animationVisibility

    val recyclerVisibility: LiveData<Int>
        get() = _recyclerVisibility

    val searchText: LiveData<String>
        get() = _searchText

    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                offlineRepository.getAllId().collect {
                    idSet = HashSet(it)
                }
            }
        }
        _loading.postValue(View.GONE)
        _animationVisibility.postValue(View.GONE)
    }

    fun getData(product: String,type: String) {
        checkIsEqual(product,type)
    }

    private fun checkIsEqual(product: String, type: String) {
        val stringCustom = "t-shirts,shoes,accessories"
        when {
            stringCustom.contains(product.lowercase()) -> {
                Log.i("getData","contain data")
                Log.i("getData",product)
                getProductsByType(product)
            }
            type.contains(product.lowercase()) -> {
                getProductsByVendor(product)
            }
            else -> {
                _recyclerVisibility.postValue(View.GONE)
                _animationVisibility.postValue(View.VISIBLE)
                Log.i("getData","not get data")
            }
        }
    }

   private fun getProductsByType(productType: String) {
       _loading.postValue(View.VISIBLE)
       _animationVisibility.postValue(View.GONE)
       _recyclerVisibility.postValue(View.GONE)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromType(productType)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        searchProductAdapter.loadData(ArrayList())
                        result.data?.product?.let {
                            _recyclerVisibility.postValue(View.VISIBLE)
                            searchProductAdapter.loadData(it)
                            _loading.postValue(View.GONE)
                        }
                    }
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loading.postValue(View.GONE)
                    _animationVisibility.postValue(View.VISIBLE)
                }
                is Result.Loading -> {
                    _loading.postValue(View.GONE)
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

   private fun getProductsByVendor(vendor: String) {
       _loading.postValue(View.VISIBLE)
       _animationVisibility.postValue(View.GONE)
       _recyclerVisibility.postValue(View.GONE)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.getProductsFromVendor(vendor)) {
                is Result.Success -> {
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main) {
                        dataOfProduct.clear()
                        searchProductAdapter.loadData(ArrayList())
                        result.data?.product?.let {
                            _recyclerVisibility.postValue(View.VISIBLE)
                            searchProductAdapter.loadData(it)
                            _loading.postValue(View.GONE)
                        }
                    }
                }

                is Result.Error -> {
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loading.postValue(View.GONE)
                    _animationVisibility.postValue(View.VISIBLE)
                }
                is Result.Loading -> {
                    _loading.postValue(View.GONE)
                    Log.i("getDiscount", "Loading")
                }
            }
        }
    }

    fun navigateToDetails(product: Products) {
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

    fun shoesChipClicked() {
        getProductsByType("shoes")
        _searchText.postValue("shoes")
    }

    fun shirtChipClicked() {
        getProductsByType("t-shirts")
        _searchText.postValue("t-shirts")

    }

    fun accessoriesChipClicked() {
        getProductsByType("accessories")
        _searchText.postValue("accessories")
    }

    fun addToWishList(products: Products, image: String) {
        viewModelScope.launch(Dispatchers.IO) {
            offlineRepository.addToWishList(
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
            offlineRepository.removeFromWishList(id)
        }

    }

    fun isLogin():Boolean = modelRepository.isLogin()
    fun navigateToLogin(){
        _navigateToLogin.postValue(Event(true))
    }

}
class SearchViewModelFactory (
    private val modelRepository: ModelRepo,
    private val offlineRepository: OfflineRepo
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (SearchViewModel(modelRepository,offlineRepository) as T)}