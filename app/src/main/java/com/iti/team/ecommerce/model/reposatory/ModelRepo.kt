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
}