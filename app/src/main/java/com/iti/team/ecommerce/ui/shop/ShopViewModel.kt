package com.iti.team.ecommerce.ui.shop

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.data_classes.PriceRule
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel: ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository(null)

    private var _loading = MutableLiveData<Int>()
    private var _transparentView = MutableLiveData<Int>()

    private var _showSuccessDialog = MutableLiveData<Event<String>>()

    private var _showErrorDialog = MutableLiveData<Event<String>>()

    val loading : LiveData<Int>
    get() = _loading


    val transparentView: LiveData<Int>
        get() = _transparentView

    val showSuccessDialog: LiveData<Event<String>>
        get() = _showSuccessDialog

    val showErrorDialog: LiveData<Event<String>>
        get() = _showErrorDialog

    init {
        showHideItems(View.GONE)
    }

    private fun createDiscount(discount:Discount){
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.createDiscount(discount)){
                is Result.Success->{
                    Log.i("createDiscount:", "${result.data}")
                    showHideItems(View.GONE)
                    result.data?.discount?.title?.let { _showSuccessDialog.postValue(Event(it))}
                }
                is Result.Error ->{
                    Log.e("createDiscount:", "${result.exception.message}")
                    showHideItems(View.GONE)
                    _showSuccessDialog.postValue(Event("oops failed to get discount code"))
                }
                is Result.Loading ->{
                    Log.i("createDiscount","Loading")
                    showHideItems(View.VISIBLE)
                }
            }
        }

    }

    fun getDiscount(discountId: Long){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.getDiscount(discountId)){
                is Result.Success->{
                    Log.i("getDiscount:", "${result.data?.discount?.title}")}
                is Result.Error ->{
                    Log.e("getDiscount:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("getDiscount","Loading")}
            }
        }

    }

    fun discountImageClicked(){
        Log.i("discountImageClicked","Clicked")
        showHideItems(View.VISIBLE)
        val priceRule = PriceRule(title = "SUMMERSALE10OFF",
            targetType = "line_item", targetSelection = "all", allocationMethod = "across",
            valueType = "fixed_amount", value = "-10.0",customerSelection = "all",
            startsAt = "2017-01-19T17:59:10Z")
        val discount = Discount(priceRule)
        createDiscount(discount)
    }

    private fun showHideItems(visibility: Int){
        _transparentView.postValue(visibility)
        _loading.postValue(visibility)
    }
}