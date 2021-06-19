package com.iti.team.ecommerce.fake

import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.reposatory.OfflineRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineRepo:OfflineRepo {
    override fun getAllWishListProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getCartProducts(): Flow<List<Product>> {
        return flow { emit(listOf<Product>()) }
    }

    override fun getAllId(): Flow<List<Long>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToWishList(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromWishList(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun addToCart(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromCart(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromCartbycount(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun reset() {
        TODO("Not yet implemented")
    }
}