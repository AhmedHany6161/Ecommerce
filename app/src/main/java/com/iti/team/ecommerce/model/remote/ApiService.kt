package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.MainCategories
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

@Suppress("DeferredIsResult")
interface ApiService {

    @GET("custom_collections.json")
    fun getMainCategories(): Deferred<Response<MainCategories>>

}