package com.iti.team.ecommerce.model.local.room

import android.app.Application
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

interface OfflineDB {

    fun getAllProducts(): Flow<List<Product>>

    suspend fun addToWishList(product: Product)

    suspend fun removeFromWishList(product: Product)

    suspend fun reset()
}