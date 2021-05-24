package com.iti.team.ecommerce.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {

    private val  modelRepository:ModelRepo = ModelRepository()

    fun getCategories(){
        viewModelScope.launch {
           val result = modelRepository.getMainCategories()
            Log.i("ViewModel","getCategories: ${result?.collections}")
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