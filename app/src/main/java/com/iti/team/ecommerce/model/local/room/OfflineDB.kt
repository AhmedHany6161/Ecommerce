package com.iti.team.ecommerce.model.local.room

import android.app.Application
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

interface OfflineDB {

    fun getAllProducts(): Flow<List<Product>>

    fun getAllId(): Flow<List<Long>>

    suspend fun addToWishList(product: Product)

    suspend fun removeFromWishList(id: Long)

    suspend fun reset()
}