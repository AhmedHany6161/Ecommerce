package com.iti.team.ecommerce.model.reposatory.offline

import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDB
import kotlinx.coroutines.flow.Flow

class OfflineRepository private constructor(private val offlineDB: OfflineDB) : OfflineRepo {
    companion object {
        private var INSTANCE: OfflineRepository? = null
        fun getInstance(offlineDatabase: OfflineDB): OfflineRepository =
            INSTANCE ?: OfflineRepository(offlineDatabase)
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return offlineDB.getAllProducts()
    }

    override suspend fun addToWishList(product: Product) {
        offlineDB.addToWishList(product)
    }

    override suspend fun removeFromWishList(product: Product) {
        offlineDB.removeFromWishList(product)

    }

    override suspend fun reset() {
        offlineDB.reset()
    }
}