package com.iti.team.ecommerce.model.remote

import android.util.Log
import com.iti.team.ecommerce.model.data_classes.MainCategories
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException


class ApiDataSource: ApiInterface {

    private  var _apiService: ApiService = NetworkService.createRetrofit()
        .create(ApiService::class.java)

    override suspend fun getMainCategories(): Deferred<Response<MainCategories>> {
       return _apiService.getMainCategories()

    }

}