package com.iti.team.ecommerce.model.reposatory

import android.util.Log
import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import com.iti.team.ecommerce.model.remote.ApiDataSource
import com.iti.team.ecommerce.model.remote.ApiInterface
import java.io.IOException


class ModelRepository: ModelRepo {
    private val apiDataSource: ApiInterface = ApiDataSource()
    override suspend fun getMainCategories():MainCategories?{

        var result:MainCategories? = null

        try {
            val response = apiDataSource.getMainCategories()
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

    override suspend fun getProducts(collectionId: Long): ProductsModel? {
        var result:ProductsModel? = null

        try {
            val response = apiDataSource.getProducts(collectionId)
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