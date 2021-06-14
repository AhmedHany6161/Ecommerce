package com.iti.team.ecommerce.model.local.room

import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

interface OfflineDB {

    fun getWishList(): Flow<List<Product>>

    fun getCart(): Flow<List<Product>>

    fun getAllId(): Flow<List<Long>>

    suspend fun addToWishList(product: Product)

    suspend fun removeFromWishList(product: Product)

    suspend fun addToCart(product: Product)

    suspend fun removeFromCart(product: Product)
    suspend fun removeFromCartbycount(product: Product)

    suspend fun getById(id: Long):Product?

    suspend fun reset()
}