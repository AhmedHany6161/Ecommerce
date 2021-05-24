package com.iti.team.ecommerce.model.reposatory

import android.util.Log
import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.remote.ApiDataSource
import com.iti.team.ecommerce.model.remote.ApiInterface
import com.iti.team.ecommerce.model.remote.Result
import java.io.IOException


class ModelRepository(): ModelRepo {
    private val apiDataSource: ApiInterface = ApiDataSource()
    override suspend fun getMainCategories():MainCategories?{

        var result:MainCategories? = null

        try {
            val response = apiDataSource.getMainCategories().await()
            if(response.isSuccessful){
                 result = response.body()
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
            }
        }catch (e: IOException){
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
      return result
    }

}