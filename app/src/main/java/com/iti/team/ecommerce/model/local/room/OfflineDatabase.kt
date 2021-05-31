package com.iti.team.ecommerce.model.local.room

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

class OfflineDatabase private constructor(): OfflineDB {
    companion object {
        private lateinit var db:ProductsDAO
        private var INSTANCE: OfflineDatabase? = null
        @JvmStatic
        fun getInstance(context: Context): OfflineDatabase =
            INSTANCE ?: buildDatabase(context)
        @JvmStatic
        private fun buildDatabase(context: Context):OfflineDatabase {
            db= Room.databaseBuilder(
                context,
                AppDatabase::class.java, "store.db"
            ).build().productDao()
            return OfflineDatabase()
        }

    }

    override fun getAllProducts(): Flow<List<Product>> {
       return db.getAllProducts()
    }

    override suspend fun addToWishList(product: Product) {
           db.addToWishList(product)
    }

    override suspend fun removeFromWishList(product: Product) {
          db.removeFromWishList(product)
    }

    override suspend fun reset() {
        db.reset()
    }

}