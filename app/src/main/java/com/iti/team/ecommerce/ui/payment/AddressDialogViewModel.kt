package com.iti.team.ecommerce.ui.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.utils.extensions.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressDialogViewModel(application: Application):AndroidViewModel(application) {

    val  modelRepository: ModelRepository =
        ModelRepository(null,application)

    private var _addressAdded = MutableLiveData<Event<Boolean>>()


    val addressAdded: LiveData<Event<Boolean>>
        get() = _addressAdded

    fun addOrder( orderWithDiscount:Boolean, productList:List<Product>,
                financialStatus:String, address:String) {
        val addOrderModel: AddOrderModel
            val billingShippingAddress = BillingShippingAddress("address",
        "address",address,"city",
        "country")
        if (orderWithDiscount){
            val lineItems:MutableList<LineItems> = mutableListOf()
            for (i in productList){
                val item = LineItems(i.variant_id, i.count.toLong())
                lineItems.add(item)
            }
            val discountCodes = listOf(DiscountCodes("SUMMERSALE10OFF", "10.0"))
            val order = SendedOrder(
                email = "mm@email.com",
                lineItems = lineItems,
                financialStatus = financialStatus,
                discountCodes = discountCodes,
                billingAddress = billingShippingAddress,
                shippingAddress = billingShippingAddress
            )

            addOrderModel = AddOrderModel(order)
        }else{

            val lineItems:MutableList<LineItems> = mutableListOf()
            for (i in productList){
                val item = LineItems(i.variant_id, i.count.toLong())
                lineItems.add(item)
            }
            val order = SendedOrder(
                email = "mm@email.com",
                lineItems = lineItems,
                financialStatus = financialStatus,
                billingAddress = billingShippingAddress,
                shippingAddress = billingShippingAddress
            )

            addOrderModel = AddOrderModel(order)
        }


        viewModelScope.launch(Dispatchers.IO) {
            when (val result = modelRepository.addOrder(order = addOrderModel)) {
                is Result.Success-> {
                    Log.i("addOrder:", "${result.data?.order}")
                    modelRepository.setAddress(address)
                    _addressAdded.postValue(Event(true))
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
}