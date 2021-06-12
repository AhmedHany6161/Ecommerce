package com.iti.team.ecommerce.ui.payment

import android.app.Application
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.iti.team.ecommerce.model.remote.Result

class PaymentViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(null,application)

    private var _buttonBackClicked = MutableLiveData<Event<Boolean>>()
    private var _discountVisibility = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Int>()
    private var _applyVisibility = MutableLiveData<Int>()
    private var _couponText = MutableLiveData<String>()
    private var _couponTextColor = MutableLiveData<Int>()

    val buttonBackClicked: LiveData<Event<Boolean>>
        get() = _buttonBackClicked

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

    init {
        _discountVisibility.postValue(View.GONE)
        _loadingVisibility.postValue(View.GONE)
        _couponTextColor.postValue(Color.BLACK)
    }

    fun getDiscount(discountId: Long){
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.getDiscount(discountId)){
                is Result.Success->{
                    Log.i("getDiscount:", "${result.data?.discount?.title}")
                    _discountVisibility.postValue(View.VISIBLE)
                    _loadingVisibility.postValue(View.GONE)
                    _applyVisibility.postValue(View.VISIBLE)
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

    fun addOrder() {
        val lineItems = listOf(LineItems(39853312639174, 1))

        val discountCodes = listOf(DiscountCodes("FAKE30", "10.0"))
        val order = SendedOrder(
            email = "tttt@tes.com",
            lineItems = lineItems,
            financialStatus = "paid",
            discountCodes = discountCodes
        )

        val addOrderModel = AddOrderModel(order)

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

    fun applyButtonClicked(){
        val id = modelRepository.getDiscountId()
        if(!couponText.value.isNullOrBlank() &&
            _couponText.value.equals("coupon must not be empty") ){
            if(id > 0){
                _loadingVisibility.postValue(View.VISIBLE)
                _applyVisibility.postValue(View.GONE)
                getDiscount(id)
            }else{
                _couponText.postValue("not valid code")
                _couponTextColor.postValue(Color.RED)
            }
        }else{
            _couponTextColor.postValue(Color.RED)
            _couponText.postValue("coupon must not be empty")
        }
    }

    fun cashOnDeliveryClicked(){

        Log.i("PaymentViewModel","cashOnDeliveryClicked")
    }

}