package com.iti.team.ecommerce.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.data_classes.PriceRule
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {

    private val  modelRepository:ModelRepo = ModelRepository()

    fun getCategories(){
        viewModelScope.launch {
           when(val result = modelRepository.getMainCategories()){
                is Result.Success->{Log.i("getCategories:", "${result.data}")}
                is Result.Error ->{Log.e("getCategories:", "${result.exception.message}")}
                is Result.Loading ->{Log.i("getCategories","Loading")}
            }
        }

    }

//    fun getProducts(collectionID: Long){
//
//        viewModelScope.launch {
//            when(val result = modelRepository.getProducts(collectionID)){
//                is Result.Success -> {
//                    Log.i(
//                        "getCategories:", "${
//                            result.data?.let {
//                                it.product[0].title
//                            }
//                        }")
//                }
//                is Result.Error ->{Log.e("getCategories:", "${result.exception.message}")}
//                is Result.Loading ->{Log.i("getCategories","Loading")}
//            }
//        }
//
//    }

}