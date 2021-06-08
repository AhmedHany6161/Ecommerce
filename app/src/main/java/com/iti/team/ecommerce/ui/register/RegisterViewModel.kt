package com.iti.team.ecommerce.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application): AndroidViewModel(application)  {

    private val  modelRepository: ModelRepo =
        ModelRepository(null,application.applicationContext)

    fun createCustomer(customer:CustomerModel){
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = modelRepository.createCustomer(customer)){
                is Result.Success->{
                    Log.i("createCustomer:", "${result.data}")}
                is Result.Error ->{
                    Log.e("createCustomer:", "${result.exception.message}")}
                is Result.Loading ->{
                    Log.i("createCustomer","Loading")}
            }
        }

    }
}