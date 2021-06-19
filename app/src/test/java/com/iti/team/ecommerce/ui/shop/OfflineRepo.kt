package com.iti.team.ecommerce.ui.shop

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.OfflineRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OfflineRepo:OfflineRepo {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = OfflineDatabase.getInstance(context)
    override fun getAllWishListProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getCartProducts(): Flow<List<Product>> {
        return flow { emit(listOf<Product>()) }
    }

    override fun getAllId(): Flow<List<Long>> {
        return flow { emit(listOf<Long>()) }
    }

    override suspend fun addToWishList(product: Product) {
        CoroutineScope(Dispatchers.IO).launch{
            db.addToWishList(product)
        }

    }

    override suspend fun removeFromWishList(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            db.removeFromWishList( Product(12, "ssss", "sss", "ssss","12"))
        }

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