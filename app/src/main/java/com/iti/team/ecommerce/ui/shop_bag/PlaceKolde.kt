package com.iti.team.ecommerce.ui.shop_bag

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//
//fun addOrder(){
//
//    val lineItems = listOf(LineItems(39853312639174,1))
//    val billingShippingAddress = BillingShippingAddress("test",
//        "test","test address","cario",
//        "country")
//    val discountCodes = listOf(DiscountCodes("FAKE30","10.0"))
//    val order = SendedOrder(email = "tttt@tes.com",billingAddress =billingShippingAddress,
//        shippingAddress = billingShippingAddress,lineItems = lineItems, financialStatus = "paid"
//        ,discountCodes = discountCodes)
//
//    val addOrderModel = AddOrderModel(order)
//
//    viewModelScope.launch(Dispatchers.IO)  {
//        when(val result = modelRepository.addOrder(order = addOrderModel)){
//            is Result.Success->{
//                Log.i("addOrder:", "${result.data?.order}")}
//            is Result.Error ->{
//                Log.e("addOrder:", "${result.exception.message}")}
//            is Result.Loading ->{
//                Log.i("addOrder","Loading")}
//        }
//    }
//}
//
//fun getOrder(){
//    viewModelScope.launch(Dispatchers.IO)  {
//        when(val result = modelRepository.getOrders("tttt@tes.com")){
//            is Result.Success->{
//                Log.i("getOrder:", "${result.data?.order}")
//                Log.i("getOrder:", "${result.data?.order?.size}")
//            }
//
//            is Result.Error ->{
//                Log.e("getOrder:", "${result.exception.message}")}
//            is Result.Loading ->{
//                Log.i("getOrder","Loading")}
//        }
//    }
//}