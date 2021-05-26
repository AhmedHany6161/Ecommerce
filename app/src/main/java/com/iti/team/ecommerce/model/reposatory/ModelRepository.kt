package com.iti.team.ecommerce.model.reposatory


import android.util.Log
import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import com.iti.team.ecommerce.model.remote.ApiDataSource
import com.iti.team.ecommerce.model.remote.ApiInterface
import java.io.IOException
import com.iti.team.ecommerce.model.remote.Result


class ModelRepository: ModelRepo {
    private val apiDataSource: ApiInterface = ApiDataSource()
    override suspend fun getMainCategories():Result<MainCategories?>{

        var result:Result<MainCategories?> = Result.Loading

        try {
            val response = apiDataSource.getMainCategories()
            if(response.isSuccessful){
                 result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else {
                //result = Result.Error(response.errorBody().)
                Log.i("ModelRepository","Error${response.errorBody()}")
            }
        }catch (e: IOException){
            result = Result.Error(e)
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