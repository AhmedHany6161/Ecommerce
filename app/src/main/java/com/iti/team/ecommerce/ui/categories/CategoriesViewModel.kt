package com.iti.team.ecommerce.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel( application: Application): AndroidViewModel(application)  {

    private val  modelRepository:ModelRepo =
        ModelRepository(null,application.applicationContext)

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO)  {
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
//                        "getProducts:", "${
//                            result.data?.let {
//                                it.product[0].title
//                            }
//                        }")
//                }
//                is Result.Error ->{Log.e("getProducts:", "${result.exception.message}")}
//                is Result.Loading ->{Log.i("getProducts","Loading")}
//            }
//        }
//
//    }

//    fun getProductsFromType(productType: String){
//
//        viewModelScope.launch(Dispatchers.IO) {
//            when(val result = modelRepository.getProductsFromType(productType)){
//                is Result.Success -> {
//                    Log.i(
//                        "getProductsFromType:", "${
//                            result.data?.let {
//                                it.product[0].title
//                            }
//                        }")
//                }
//                is Result.Error ->{Log.e("getProductsFromType:", "${result.exception.message}")}
//                is Result.Loading ->{Log.i("getProductsFromType","Loading")}
//            }
//        }
//
//    }



}