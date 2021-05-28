package com.iti.team.ecommerce.model.reposatory


import android.util.Log
import com.iti.team.ecommerce.model.data_classes.*
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

    override suspend fun getProducts(collectionId: Long): Result<ProductsModel?> {
        var result:Result<ProductsModel?> = Result.Loading

        try {
            val response = apiDataSource.getProducts(collectionId)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result

    }

    override suspend fun createDiscount(discount: Discount): Result<Discount?> {
        var result:Result<Discount?> = Result.Loading

        try {
            val response = apiDataSource.createDiscount(discount)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result

    }

    override suspend fun getDiscount(discountId: Long): Result<Discount?> {
        var result:Result<Discount?> = Result.Loading

        try {
            val response = apiDataSource.getDiscount(discountId)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result
    }

    override suspend fun getProductImages(productId: Long): Result<ProductImages?> {
        var result:Result<ProductImages?> = Result.Loading

        try {
            val response = apiDataSource.getProductImages(productId)

            if (response.isSuccessful){
                result = Result.Success(response.body())
            }

        }catch (e: IOException){
            result = Result.Error(e)
        }
        return  result
    }

    override suspend fun createCustomer(customer: CustomerModel): Result<CustomerModel?> {
        var result:Result<CustomerModel?> = Result.Loading

        try {
            val response = apiDataSource.createCustomer(customer)

            if (response.isSuccessful){
                result = Result.Success(response.body())
            }

        }catch (e: IOException){
            result = Result.Error(e)
        }
        return  result

    }

    override suspend fun getProductsFromType(productType: String): Result<ProductsModel?> {
        var result:Result<ProductsModel?> = Result.Loading

        try {
            val response = apiDataSource.getProductsFromType(productType)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result

    }
}