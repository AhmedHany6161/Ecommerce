package com.iti.team.ecommerce.model.local.room

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
        private fun buildDatabase(context: Context): OfflineDatabase {
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "store.db"
            ).build().productDao()
            INSTANCE = OfflineDatabase()
            return INSTANCE!!
        }

    }

    override fun getWishList(): Flow<List<Product>> = db.getWishList()

    override fun getCart(): Flow<List<Product>> = db.getCart()

    override fun getAllId(): Flow<List<Long>> = db.getAllWishListIds()


    override suspend fun addToWishList(product: Product) {
        product.inWish = true
        db.add(product)
    }

    override suspend fun removeFromWishList(product: Product) {
        if (product.inCart) {
            product.inWish = false
            db.update(product)
        } else {
            db.remove(product.id)
        }
    }

    override suspend fun addToCart(product: Product) {
        if (product.inCart) {
            if (product.count > 0) {
                product.count += 1
            } else {
                product.count = -product.count
            }
            db.update(product)
        } else {
            product.inCart = true
            if (product.count == 0) {
                product.count = 1
            } else {
                product.count = -product.count
            }
            db.add(product)
        }
    }

    override suspend fun removeFromCart(product: Product) {
        if (product.inWish) {
            product.inCart = false
            product.count = 0
            db.update(product)
        } else {
            db.remove(product.id)
        }
    }

    override suspend fun removeFromCartbycount(product: Product) {
        if(product.count > 0){
            product.count -= 1
            db.update(product)
        }else{
            if (product.inWish) {
                product.inCart = false
                product.count = 0
                db.update(product)
            } else {
                db.remove(product.id)
            }
        }
    }

    override suspend fun getById(id: Long): Product? = db.getById(id)


    override suspend fun reset() = db.reset()

}