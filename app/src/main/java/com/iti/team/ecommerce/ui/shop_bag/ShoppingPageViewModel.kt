package com.iti.team.ecommerce.ui.shop_bag

import android.app.AlertDialog
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingPageViewModel (application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)

    private var dataOfProduct: List<Product> = mutableListOf()
    private val productFlowData: MutableLiveData<List<Product>> by lazy { MutableLiveData() }
    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    val buttonBackClicked:LiveData<Event<Boolean>>
    get() = _buttonBackClicked
    var total = 0.0
    private val _addToWish = MutableLiveData<String>()
    val addToWish: LiveData<String> get() = _addToWish
    private val isValid = MutableLiveData<Boolean>()
    fun isValid(): LiveData<Boolean> = isValid

    private val mutableTotalPrice = MutableLiveData<Double>()
    fun total_price(): LiveData<Double> = mutableTotalPrice

    init {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.getCartProducts().collect {
                dataOfProduct = it
                productFlowData.postValue(dataOfProduct)
                println(dataOfProduct)
                calculateCartTotal(it)
            }
        }
    }


    fun removeFromCard(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeFromCart(id)
        }
    }

    fun removebyCount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeFromCartbycount(id)
        }
    }

    fun getCardList(): LiveData<List<Product>> {
        return productFlowData
    }

    fun addToCard(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToCart(product)
        }
    }

    fun addToWishList(product: Product) {
        _addToWish.value = "product successfully added to wishList"
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.addToWishList(product)
        }
    }



    fun backButtonClicked(){
        _buttonBackClicked.postValue(Event(true))
    }

    fun calculateCartTotal(products : List<Product>) {
        viewModelScope.launch(Dispatchers.Main)  {
                total=0.0
                for (item in products) {
                    total += (item.count)*(item.price).toDouble()
                    println("sssssssssssssssssssssssssssssssssssssssssssss")
                    println(total.toString())

                }
                mutableTotalPrice.setValue(total)
        }
    }

    fun getTotalPrice(): LiveData<Double?>? {
        if (mutableTotalPrice.getValue() == null) {
            mutableTotalPrice.setValue(0.0)
        }

        return mutableTotalPrice
    }
}



