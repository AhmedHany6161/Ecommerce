package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import retrofit2.Response

interface ModelRepo {

    suspend fun getMainCategories(): MainCategories?
    suspend fun getProducts(collectionId: Long): ProductsModel?
}