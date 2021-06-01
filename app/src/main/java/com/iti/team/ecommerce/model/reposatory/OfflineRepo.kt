package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

interface OfflineRepo {
    fun getAllProducts(): Flow<List<Product>>

    suspend fun addToWishList(product: Product)

    suspend fun removeFromWishList(product: Product)

    suspend fun reset()
}