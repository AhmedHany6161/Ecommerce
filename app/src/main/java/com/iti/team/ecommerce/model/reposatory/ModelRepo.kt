package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.MainCategories
import com.iti.team.ecommerce.model.data_classes.ProductsModel
import com.iti.team.ecommerce.model.remote.Result

interface ModelRepo {

    suspend fun getMainCategories(): Result<MainCategories?>
    suspend fun getProducts(collectionId: Long): ProductsModel?
}