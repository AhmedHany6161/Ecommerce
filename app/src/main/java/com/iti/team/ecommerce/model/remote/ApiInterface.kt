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
}