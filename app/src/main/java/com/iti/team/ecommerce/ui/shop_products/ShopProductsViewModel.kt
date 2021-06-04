package com.iti.team.ecommerce.ui.shop_products

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.ui.shop.ShopAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopProductsViewModel:ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository(null)
    var shopProductAdapter = ShopProductAdapter(this)
    private var _shopProductImage = MutableLiveData<String>()
    private var dataOfProduct: MutableList<Pair<Products, String>> = mutableListOf()

    private var _loading = MutableLiveData<Int>()


    val shopProductImage: LiveData<String>
        get() = _shopProductImage

    val loading : LiveData<Int>
        get() = _loading

    fun getData(imageUrl: String){
        _shopProductImage.postValue(imageUrl)
    }

    private suspend fun fetchImages(list: List<Products>) {
        list.forEach {
            when (val im = modelRepository.getProductImages(it.productId!!)) {
                is Result.Success -> {
                    dataOfProduct.add(Pair(it, im.data?.images?.get(0)?.src!!))
                }
            }
        }
        shopProductAdapter.loadData(dataOfProduct)

    }
    fun getProducts(collectionId:Long){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.getProducts(collectionId)){
                is Result.Success->{
                    Log.i("getProducts:", "${result.data?.product}")
                    withContext(Dispatchers.Main){
                        result.data?.product?.let {
                               fetchImages(it)
                            _loading.postValue(View.GONE)
                                //shopProductAdapter.loadData(Pair(it,""))
                        }
                    }
                }

                is Result.Error ->{
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loading.postValue(View.GONE)
                }
                is Result.Loading ->{
                    _loading.postValue(View.GONE)
                    Log.i("getDiscount","Loading")}
            }
        }

    }
}