package com.iti.team.ecommerce.ui.shop


import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopViewModel: ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository(null)
    private var code:String = ""
    var shopAdapter = ShopAdapter(this)

    private var _loading = MutableLiveData<Int>()
    private var _transparentView = MutableLiveData<Int>()

    private var _showSuccessDialog = MutableLiveData<Event<String>>()

    private var _showErrorDialog = MutableLiveData<Event<String>>()

    private var _dialogImage = MutableLiveData<Int>()
    private var _dialogButtonText = MutableLiveData<String>()

    private var _dialogMessage = MutableLiveData<String>()

    private var _copyIcon = MutableLiveData<Int>()
    private var _messageAlignment = MutableLiveData<Int>()

    private var _copyAction = MutableLiveData<Event<String>>()

    private var _navigateToWish = MutableLiveData<Event<Boolean>>()
    private var _navigateToCart = MutableLiveData<Event<Boolean>>()
    private var _navigateToSearch = MutableLiveData<Event<String>>()
    private var _navigateToShopProduct = MutableLiveData<Event<Pair<Long,String>>>()

    private var productTypeSet: HashSet<String> = hashSetOf()

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

    val copyAction: LiveData<Event<String>>
        get() = _copyAction

    val navigateToWish: LiveData<Event<Boolean>>
        get() = _navigateToWish

    val navigateToCart: LiveData<Event<Boolean>>
        get() = _navigateToCart

    val navigateToSearch: LiveData<Event<String>>
        get() = _navigateToSearch

    val navigateToShopProduct: LiveData<Event<Pair<Long,String>>>
        get() = _navigateToShopProduct

    init {
        showHideItems(View.GONE)
//        productTypeSet.add("t-shirts")
//        productTypeSet.add("shoes")
//        productTypeSet.add("accessories")
        addOrder()
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

    fun smartCollection(){

        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.smartCollection()){
                is Result.Success->{
                    Log.i("getDiscount:", "${result.data?.smart_collections}")
                    withContext(Dispatchers.Main){
                        result.data?.smart_collections?.let {
                            shopAdapter.loadData(it)
                            for(i in it){
                                i.handle?.let { it1 -> productTypeSet.add(it1) }
                            }
                        }
                    }
                }

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
        _copyAction.postValue(Event(code))
    }

    private fun showHideItems(visibility: Int){
        _transparentView.postValue(visibility)
        _loading.postValue(visibility)
    }
    fun shopItemsClicked(id: Long,image:String){
        Log.i("shopItemsClicked",id.toString())
        Log.i("shopItemsClicked",image)
        _navigateToShopProduct.postValue(Event(Pair(id,image)))

    }

    fun navigateToWish(){
        _navigateToWish.postValue(Event(true))
    }

    fun navigateToCart(){
        _navigateToCart.postValue(Event(true))
    }

    fun navigateToSearch(){
        Log.i("navigateToSearch","${productTypeSet.size}")
        _navigateToSearch.postValue(Event(productTypeSet.toString()))
        Log.i("navigateToSearch","$productTypeSet")
    }

    fun addOrder(){
        val lineItems = listOf(LineItems(39853312639174,1))
        val billingShippingAddress = BillingShippingAddress("test",
        "test","test address","cario",
        "country")
        val discountCodes = listOf(DiscountCodes("FAKE30","10.0"))
        val order = SendedOrder(email = "www@tes.com",billingAddress =billingShippingAddress,
        shippingAddress = billingShippingAddress,lineItems = lineItems, financialStatus = "paid"
            ,discountCodes = discountCodes)
        val addOrderModel = AddOrderModel(order)
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.addOrder(order = addOrderModel)){
                is Result.Success->{
                    Log.i("addOrder:", "${result.data?.order}")}
                is Result.Error ->{
                    Log.e("addOrder:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("addOrder","Loading")}
            }
        }
    }

}