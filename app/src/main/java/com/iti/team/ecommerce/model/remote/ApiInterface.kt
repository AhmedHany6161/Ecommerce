package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.MainCategories
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiInterface {

    suspend fun getMainCategories(): Deferred<Response<MainCategories>>
}