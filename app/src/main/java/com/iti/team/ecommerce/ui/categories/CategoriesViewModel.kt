package com.iti.team.ecommerce.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
//    fun getProducts(){
//        viewModelScope.launch {
//            val result:ProductsModel? = modelRepository.getProducts(268359598278)
//            Log.i("ViewModel","getProducts: ${result?.let { it.product[0].title }}")
//        }
//
//    }

}