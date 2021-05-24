package com.iti.team.ecommerce.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {

    private val  modelRepository:ModelRepo = ModelRepository()

    fun getCategories(){
        viewModelScope.launch {
           val result = modelRepository.getMainCategories()
            Log.i("ViewModel","getCategories: ${result}")
        }

    }

}