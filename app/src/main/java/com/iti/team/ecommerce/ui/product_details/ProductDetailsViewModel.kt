package com.iti.team.ecommerce.ui.product_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel: ViewModel() {
    private val  modelRepository: ModelRepo = ModelRepository()

    fun getProductImage(productId: Long){

        viewModelScope.launch {
            when(val result = modelRepository.getProductImages(productId)){
                is Result.Success -> {
                    Log.i(
                        "getProductImage:", "${
                            result?.let {
                                it.data?.images?.let {
                                    it[0].src
                                }
                            }

                        }")
                }
                is Result.Error ->{
                    Log.e("getCategories:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("getCategories","Loading")}
            }
        }

    }
}