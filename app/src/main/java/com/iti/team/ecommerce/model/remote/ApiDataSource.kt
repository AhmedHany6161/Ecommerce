package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import retrofit2.Response


class ApiDataSource: ApiInterface {

    private  var _apiService: ApiService = NetworkService.createRetrofit()
        .create(ApiService::class.java)

    override suspend fun getMainCategories(): Response<MainCategories>{
        return _apiService.getMainCategories()

    }

    override suspend fun getProducts(collectionId: Long): Response<ProductsModel> {
        return _apiService.getProducts(collectionId)
    }

    override suspend fun createDiscount(discount: Discount): Response<Discount> {
        return  _apiService.createDiscount(discount)
    }

    override suspend fun getDiscount(discountId: Long): Response<Discount> {
        return _apiService.getDiscount(discountId)
    }

}