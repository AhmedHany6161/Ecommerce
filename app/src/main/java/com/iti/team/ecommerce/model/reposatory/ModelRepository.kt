package com.iti.team.ecommerce.model.reposatory


import android.util.Log
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.local.room.OfflineDB
import com.iti.team.ecommerce.model.remote.ApiDataSource
import com.iti.team.ecommerce.model.remote.ApiInterface
import com.iti.team.ecommerce.model.remote.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException


class ModelRepository(private val offlineDB: OfflineDB?): ModelRepo , OfflineRepo {
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

    override suspend fun smartCollection(): Result<SmartCollectionModel?> {
        var result:Result<SmartCollectionModel?> = Result.Loading

        try {
            val response = apiDataSource.smartCollection()
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

    override suspend fun updateCustomer(
        customerId: Long,
        customer: CustomerModel
    ): Result<CustomerModel?> {
        var result:Result<CustomerModel?> = Result.Loading

        try {
            val response = apiDataSource.updateCustomer(customerId,customer)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","error ${response.code()}")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result
    }

    override suspend fun login(email: String): Result<CustomerLoginModel?> {
        var result:Result<CustomerLoginModel?> = Result.Loading

        try {
            val response = apiDataSource.login(email)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","error ${response.code()}")
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result
    }

    override suspend fun getProductsFromVendor(vendor: String): Result<ProductsModel?> {
        var result:Result<ProductsModel?> = Result.Loading

        try {
            val response = apiDataSource.getProductsFromVendor(vendor)
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

    override suspend fun addOrder(order: AddOrderModel): Result<GettingOrderModel?> {
        var result:Result<GettingOrderModel?> = Result.Loading

        try {
            val response = apiDataSource.addOrder(order)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
                Log.i("ModelRepository",response.code().toString())
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result
    }

    override suspend fun getOrders(email: String): Result<OrdersModels?> {
        var result:Result<OrdersModels?> = Result.Loading

        try {
            val response = apiDataSource.getOrders(email)
            if(response.isSuccessful){
                result = Result.Success(response.body())
                Log.i("ModelRepository","Result $result")
            }else{
                Log.i("ModelRepository","Error")
                Log.i("ModelRepository",response.code().toString())
            }
        }catch (e: IOException){
            result = Result.Error(e)
            Log.e("ModelRepository","IOException ${e.message}")
            Log.e("ModelRepository","IOException ${e.localizedMessage}")

        }
        return result
    }


    override fun getAllWishListProducts(): Flow<List<Product>> {
        return offlineDB?.getAllProducts() ?: flow { emit(listOf<Product>()) }
    }

    override fun getAllId(): Flow<List<Long>> {
        return offlineDB?.getAllId() ?: flow { emit(listOf<Long>()) }
    }

    override suspend fun addToWishList(product: Product) {
        offlineDB?.addToWishList(product)
    }

    override suspend fun removeFromWishList(id: Long) {
        offlineDB?.removeFromWishList(id)

    }

    override suspend fun reset() {
        offlineDB?.reset()
    }
}