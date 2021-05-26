package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiInterface {

    suspend fun getMainCategories(): Response<MainCategories>
    suspend fun getProducts(collectionId: Long): Response<ProductsModel>
    suspend fun createDiscount(discount: Discount): Response<Discount>
    suspend fun getDiscount(discountId: Long): Response<Discount>
}