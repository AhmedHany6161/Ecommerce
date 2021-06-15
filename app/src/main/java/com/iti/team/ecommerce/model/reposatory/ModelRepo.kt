package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.Result
import retrofit2.Response

interface ModelRepo {

    suspend fun getMainCategories(): Result<MainCategories?>
    suspend fun getProducts(collectionId: Long): Result<ProductsModel?>

    suspend fun createDiscount(discount: Discount): Result<Discount?>
    suspend fun getDiscount(discountId: Long): Result<Discount?>

    suspend fun getProductImages(productId: Long): Result<ProductImages?>

    suspend fun createCustomer(customer: CustomerModel): Result<CustomerModel?>

    suspend fun getProductsFromType(productType: String): Result<ProductsModel?>

    suspend fun smartCollection(): Result<SmartCollectionModel?>

    suspend fun updateCustomer(customerId: Long,customer: CustomerModel): Result<CustomerModel?>

    suspend fun login(email: String): Result<CustomerLoginModel?>

    suspend fun getProductsFromVendor(vendor: String): Result<ProductsModel?>

    suspend fun addOrder(order: AddOrderModel): Result<GettingOrderModel?>

    suspend fun getOrders(email:String): Result<OrdersModels?>

    fun isLogin():Boolean
    fun setLogin(login:Boolean)

    fun getDiscountId():Long
    fun setDiscount(id:Long)

    fun setAddress(address:String)
    fun getAddress():String

    fun setEmail(email:String)
    fun getEmail():String

    fun setUserName(userName:String)
    fun getUserName():String
}