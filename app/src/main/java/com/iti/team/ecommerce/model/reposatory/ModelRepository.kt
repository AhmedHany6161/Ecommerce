package com.iti.team.ecommerce.model.reposatory


import android.content.Context
import android.util.Log
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.local.preferances.MySharedPreference
import com.iti.team.ecommerce.model.local.preferances.Preference
import com.iti.team.ecommerce.model.local.preferances.PreferenceDataSource
import com.iti.team.ecommerce.model.local.room.OfflineDB
import com.iti.team.ecommerce.model.remote.ApiDataSource
import com.iti.team.ecommerce.model.remote.ApiInterface
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.utils.PREF_FILE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception


class ModelRepository(private val offlineDB: OfflineDB?,val context: Context): ModelRepo , OfflineRepo {
    private val apiDataSource: ApiInterface = ApiDataSource()
    private val preference =
        MySharedPreference(context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE))

    private val sharedPreference:Preference = PreferenceDataSource(preference)

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
            }else{
                result = Result.Error(Exception(response.errorBody()?.string()))

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
        } catch (e: IOException) {
            result = Result.Error(e)
            Log.e("ModelRepository", "IOException ${e.message}")
            Log.e("ModelRepository", "IOException ${e.localizedMessage}")

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

    override suspend fun getAddress(
        customerId: Long,
        addressId: Long
    ): Result<CustomerAddressModel?> {
        var result:Result<CustomerAddressModel?> = Result.Loading

        try {
            val response = apiDataSource.getAddress(customerId,addressId)
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

    override suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        address: AddressModel
    ): Result<CustomerAddressModel?> {
        var result:Result<CustomerAddressModel?> = Result.Loading

        try {
            val response = apiDataSource.updateAddress(customerId,addressId,address)
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
    override suspend fun deleteAddress(
        customerId: Long,
        addressId: Long
    ): Result<CustomerAddressModel?> {
        var result:Result<CustomerAddressModel?> = Result.Loading

        try {
            val response = apiDataSource.deleteAddress(customerId,addressId)
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
    override suspend fun addAddress(
        customerId: Long,
        address: AddressModel
    ): Result<CustomerAddressModel?> {
        var result:Result<CustomerAddressModel?> = Result.Loading

        try {
            val response = apiDataSource.addAddress(customerId,address)
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
    override fun isLogin(): Boolean {
        return sharedPreference.isLogin()
    }

    override fun setLogin(login: Boolean) {
        sharedPreference.setLogin(login)
    }

    override fun getDiscountId(): Long {
       return sharedPreference.getDiscountId()
    }

    override fun setDiscount(id: Long) {
        sharedPreference.setDiscountId(id)
    }

    override fun setAddress(address: String) {
        sharedPreference.setAddress(address)
    }

    override fun getAddress(): String {
        return sharedPreference.getAddress()
    }

    override fun setEmail(email: String) {
        sharedPreference.setEmail(email)
    }

    override fun getEmail(): String {
        return sharedPreference.getEmail()
    }

    override fun setUserName(userName: String) {
        sharedPreference.setUserName(userName)
    }

    override fun getUserName(): String {
        return sharedPreference.getUserName()
    }


    override fun getAllWishListProducts(): Flow<List<Product>> =
        offlineDB?.getWishList() ?: flow { emit(listOf<Product>()) }

    override fun getCartProducts(): Flow<List<Product>> =
        offlineDB?.getCart() ?: flow { emit(listOf<Product>()) }

    override fun getAllId(): Flow<List<Long>> =
        offlineDB?.getAllId() ?: flow { emit(listOf<Long>()) }


    override suspend fun addToWishList(product: Product) {
        val conProduct = offlineDB?.getById(product.id)
        if (conProduct == null) {
            offlineDB?.addToWishList(product)
        } else {
            offlineDB?.addToWishList(conProduct)
        }
    }

    override suspend fun removeFromWishList(id: Long) {
        val product = offlineDB?.getById(id)
        if (product != null) {
            offlineDB?.removeFromWishList(product)
        }

    }

    override suspend fun addToCart(product: Product) {
        val conProduct = offlineDB?.getById(product.id)
        if (conProduct == null) {
            offlineDB?.addToCart(product)
        } else {
            offlineDB?.addToCart(conProduct)
        }
    }

    override suspend fun removeFromCart(id: Long) {
        val product = offlineDB?.getById(id)
        if (product != null) {
            offlineDB?.removeFromCart(product)
        }
    }
    override suspend fun removeFromCartbycount(id: Long) {
        val product = offlineDB?.getById(id)
        if (product != null) {
            offlineDB?.removeFromCartbycount(product)
        }
    }

    override suspend fun reset() {
        offlineDB?.reset()
    }
}