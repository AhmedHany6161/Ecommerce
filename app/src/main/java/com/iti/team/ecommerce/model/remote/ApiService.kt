package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @GET("custom_collections.json")
    suspend fun getMainCategories(): Response<MainCategories>

    @GET("products.json?")
    suspend fun getProducts(@Query("collection_id") collectionId: Long):
            Response<ProductsModel>

    @POST("price_rules.json")
    suspend fun createDiscount(@Body priceRule:Discount):
            Response<Discount>

    @GET("price_rules/{id}.json")
    suspend fun getDiscount(@Path("id") discountId:Long ):
            Response<Discount>

    @GET("products/{productID}/images.json")
    suspend fun getProductImage(@Path("productID") ProductId:Long ):
            Response<ProductImages>



}