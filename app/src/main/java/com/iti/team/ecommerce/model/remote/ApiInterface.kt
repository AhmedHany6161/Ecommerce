package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.*
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiInterface {

    suspend fun getMainCategories(): Response<MainCategories>
    suspend fun getProducts(collectionId: Long): Response<ProductsModel>
    suspend fun createDiscount(discount: Discount): Response<Discount>
    suspend fun getDiscount(discountId: Long): Response<Discount>
    suspend fun getProductImages(productId: Long): Response<ProductImages>
    suspend fun createCustomer(customer: CustomerModel): Response<CustomerModel>
    suspend fun getProductsFromType(productType: String): Response<ProductsModel>
    suspend fun getProductsFromVendor(vendor: String): Response<ProductsModel>
    suspend fun smartCollection(): Response<SmartCollectionModel>

    suspend fun updateCustomer(customerId: Long,customer: CustomerModel): Response<CustomerModel>
    suspend fun login(email: String): Response<CustomerLoginModel>
    suspend fun addOrder(order: AddOrderModel): Response<GettingOrderModel>

    suspend fun getOrders(email:String): Response<OrdersModels>

}