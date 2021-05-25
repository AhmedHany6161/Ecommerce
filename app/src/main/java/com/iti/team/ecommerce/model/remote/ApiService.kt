package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.PriceRule
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {


    @GET("custom_collections.json")
    suspend fun getMainCategories(): Response<MainCategories>

    @GET("products.json?")
    suspend fun getProducts(@Query("collection_id") collectionId: Long):
            Response<ProductsModel>

    @GET("price_rules.json")
    suspend fun createDiscount(@Body priceRule:PriceRule ):
            Response<ProductsModel>



}