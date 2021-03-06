package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

interface OfflineRepo {

    fun getAllWishListProducts(): Flow<List<Product>>

    fun getCartProducts(): Flow<List<Product>>

    fun getAllId(): Flow<List<Long>>

    suspend fun addToWishList(product: Product)

    suspend fun removeFromWishList(id: Long)

    suspend fun addToCart(product: Product)

    suspend fun removeFromCart(id: Long)

    suspend fun removeFromCartbycount(id: Long)

    suspend fun reset()
}