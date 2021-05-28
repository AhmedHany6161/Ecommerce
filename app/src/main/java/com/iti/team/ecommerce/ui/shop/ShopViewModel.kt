package com.iti.team.ecommerce.ui.shop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel: ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository()

    fun createDiscount(discount:Discount){
        viewModelScope.launch(Dispatchers.IO)  {
            when(val result = modelRepository.createDiscount(discount)){
                is Result.Success->{
                    Log.i("createDiscount:", "${result.data}")}
                is Result.Error ->{
                    Log.e("createDiscount:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("createDiscount","Loading")}
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
}