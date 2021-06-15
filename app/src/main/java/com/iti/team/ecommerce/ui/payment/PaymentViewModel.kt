package com.iti.team.ecommerce.ui.payment

import android.app.Application
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.utils.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

class PaymentViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(null,application)

    private var subTotalPrice: String? = ""
    private var productList:List<Product> = ArrayList()
    private var totalPrice: Double? = 0.0
    private var orderWithDiscount = false

    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    private var _openDialog = MutableLiveData<Event<AddressDialog>>()
    private var _discountVisibility = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Int>()
    private var _applyVisibility = MutableLiveData<Int>()
    private var _couponText = MutableLiveData<String>()
    private var _couponTextColor = MutableLiveData<Int>()
    private var _subTotalText = MutableLiveData<String>()
    private var _discountText = MutableLiveData<String>()
    private var _totalText = MutableLiveData<String>()

    private var _errorText = MutableLiveData<String>()
    private var _errorVisibility = MutableLiveData<Int>()

    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked

    val openDialog: LiveData<Event<AddressDialog>>
        get() = _openDialog

    val discountVisibility: LiveData<Int>
        get() = _discountVisibility

    val loadingVisibility: LiveData<Int>
        get() = _loadingVisibility

    val applyVisibility: LiveData<Int>
        get() = _applyVisibility

    val couponText: LiveData<String>
        get() = _couponText

    val couponTextColor: LiveData<Int>
        get() = _couponTextColor

    val subTotalText: LiveData<String>
        get() = _subTotalText
    val discountText: LiveData<String>
        get() = _discountText
    val totalText: LiveData<String>
        get() = _totalText

    val errorText: LiveData<String>
        get() = _errorText

    val errorVisibility: LiveData<Int>
        get() = _errorVisibility

    init {
        _discountVisibility.postValue(View.GONE)
        _loadingVisibility.postValue(View.GONE)
        _errorVisibility.postValue(View.GONE)
        //_couponTextColor.postValue(Color.GRAY)
        //_couponText.postValue("")
       // calDiscount()
    }

    fun getDiscount(discountId: Long){
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.getDiscount(discountId)){
                is Result.Success->{
                    Log.i("getDiscount:", "${result.data?.discount?.title}")
                    _discountVisibility.postValue(View.VISIBLE)
                    _loadingVisibility.postValue(View.GONE)
                    _applyVisibility.postValue(View.VISIBLE)
                    calDiscount()
                    orderWithDiscount = true
                }

                is Result.Error ->{
                    Log.e("getDiscount:", "${result.exception.message}")
                    _loadingVisibility.postValue(View.GONE)
                }
                is Result.Loading ->{
                    _loadingVisibility.postValue(View.GONE)
                    Log.i("getDiscount","Loading")}
            }
        }

    }

    fun addOrder(financialStatus:String) {
        val addOrderModel:AddOrderModel
        if (orderWithDiscount){
            val lineItems:MutableList<LineItems> = mutableListOf()
            for (i in productList){
               val item = LineItems(i.variant_id, i.count.toLong())
                lineItems.add(item)
            }
            val discountCodes = listOf(DiscountCodes("SUMMERSALE10OFF", "10.0"))
            val order = SendedOrder(
                email = "email@email.com",
                lineItems = lineItems,
                financialStatus = financialStatus,
                discountCodes = discountCodes
            )

            addOrderModel = AddOrderModel(order)
        }else{

            val lineItems:MutableList<LineItems> = mutableListOf()
            for (i in productList){
                val item = LineItems(i.variant_id, i.count.toLong())
                lineItems.add(item)
            }
            val order = SendedOrder(
                email = "email@email.com",
                lineItems = lineItems,
                financialStatus = financialStatus
            )

            addOrderModel = AddOrderModel(order)
        }


       viewModelScope.launch(Dispatchers.IO) {
           when (val result = modelRepository.addOrder(order = addOrderModel)) {
               is Result.Success-> {
                   Log.i("addOrder:", "${result.data?.order}")
               }
               is Result.Error -> {
                   Log.e("addOrder:", "${result.exception.message}")
               }
               is Result.Loading -> {
                   Log.i("addOrder", "Loading")
               }
           }
       }
    }

    fun buttonBackClicked(){
        Log.i("PaymentViewModel","buttonBackClicked")
        _buttonBackClicked.postValue(Event(true))
    }

    fun applyButtonClicked(text:String?){
        _errorVisibility.postValue(View.GONE)
        _couponTextColor.postValue(Color.BLACK)
        val id = modelRepository.getDiscountId()
        if(!text.isNullOrBlank()){
            if(id > 0 && text == "SUMMERSALE10OFF"){
                Log.i("applyButtonClicked","get data")
                _loadingVisibility.postValue(View.VISIBLE)
                _applyVisibility.postValue(View.GONE)
                getDiscount(id)
            }else{
                Log.i("applyButtonClicked","not valid")
                _errorText.postValue("not valid code")
                _errorVisibility.postValue(View.VISIBLE)
            }
        }else{
            _errorText.postValue("coupon must not be empty")
            _errorVisibility.postValue(View.VISIBLE)

        }
    }

    fun getCouponText(text:String){
        Log.i("PaymentViewModel","getCouponText")
        _couponText.postValue(text)
    }
    fun cashOnDeliveryClicked(){
        if (modelRepository.getAddress() != ""){
            Log.i("PaymentViewModel","cashOnDeliveryClicked")
            addOrder("authorized")
        }else{
            val dialog = AddressDialog.newInstance(orderWithDiscount,productList,"authorized")
            _openDialog.postValue(Event(dialog))
        }
    }

    fun getOrdersData(totalPrice:String,ordersListString:String){
        Log.i("getOrdersData","${convertStringToList(ordersListString)}")
        convertStringToList(ordersListString)
        _subTotalText.postValue("EGP $totalPrice")
        _totalText.postValue("EGP $totalPrice")
        subTotalPrice = totalPrice

    }

    private fun convertStringToList(productObject:String):List<Product>{
        val productListType = Types.newParameterizedType(List::class.java, Product::class.java)
        val adapterProductList: JsonAdapter<List<Product>> = Constants.moshi.adapter(productListType)
        productList = adapterProductList.fromJson(productObject)?: listOf()
        return productList
    }

    private fun calDiscount(){
        val discount = (subTotalPrice?.toDouble()?.div(100.0f))?.times(10)
        Log.i("getOrdersData","$discount")
         totalPrice = discount?.let { subTotalPrice?.toDouble()?.minus(it) }
        _discountText.postValue("EGP $discount")
        _totalText.postValue("EGP $totalPrice")
    }

     fun getPrice():String = totalPrice.toString()

}