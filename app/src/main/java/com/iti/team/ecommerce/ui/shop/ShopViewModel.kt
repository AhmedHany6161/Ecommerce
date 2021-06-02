package com.iti.team.ecommerce.ui.shop


import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.R
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

    private var _dialogImage = MutableLiveData<Int>()
    private var _dialogButtonText = MutableLiveData<String>()

    private var _dialogMessage = MutableLiveData<String>()

    private var _copyIcon = MutableLiveData<Int>()
    private var _messageAlignment = MutableLiveData<Int>()

    val loading : LiveData<Int>
    get() = _loading


    val transparentView: LiveData<Int>
        get() = _transparentView

    val showSuccessDialog: LiveData<Event<String>>
        get() = _showSuccessDialog

    val showErrorDialog: LiveData<Event<String>>
        get() = _showErrorDialog

    val dialogImage: LiveData<Int>
        get() = _dialogImage

    val dialogButtonText: LiveData<String>
        get() = _dialogButtonText

    val dialogMessage: LiveData<String>
        get() = _dialogMessage

    val copyIcon : LiveData<Int>
        get() = _copyIcon

    val messageAlignment : LiveData<Int>
        get() = _messageAlignment

    init {
        showHideItems(View.GONE)

    }

    private fun createDiscount(discount:Discount){
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.createDiscount(discount)){
                is Result.Success->{
                    Log.i("createDiscount:", "${result.data}")
                    _copyIcon.postValue(View.VISIBLE)
                    showHideItems(View.GONE)
                    _dialogImage.postValue(R.drawable.ic_gift_box)
                    _dialogButtonText.postValue("Cancel")
                    _messageAlignment.postValue(View.TEXT_ALIGNMENT_TEXT_START)
                    result.data?.discount?.title?.let {_dialogMessage.postValue(it)}
                    result.data?.discount?.title?.let { _showSuccessDialog.postValue(Event(it))}
                }
                is Result.Error ->{
                    Log.e("createDiscount:", "${result.exception.message}")
                    showHideItems(View.GONE)
                    _copyIcon.postValue(View.GONE)
                    _dialogImage.postValue(R.drawable.ic_warning)
                    _dialogButtonText.postValue("OK")
                    _messageAlignment.postValue(View.TEXT_ALIGNMENT_CENTER)
                    _dialogMessage.postValue("oops failed to get discount code.")
                    _showSuccessDialog.postValue(Event("oops failed to get discount code."))
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

    fun copyIconClicked(){
        Log.i("copyIconClicked","Clicked")
    }

    private fun showHideItems(visibility: Int){
        _transparentView.postValue(visibility)
        _loading.postValue(visibility)
    }
}