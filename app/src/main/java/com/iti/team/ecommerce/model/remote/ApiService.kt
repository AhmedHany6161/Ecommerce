package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {


    @GET("custom_collections.json")
    suspend fun getMainCategories(): Response<MainCategories>

    @GET("products.json?")
    suspend fun getProducts(@Query("collection_id") collectionId: Long):
            Response<ProductsModel>



}