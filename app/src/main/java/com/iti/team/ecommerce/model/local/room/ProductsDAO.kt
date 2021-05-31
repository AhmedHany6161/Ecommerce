package com.iti.team.ecommerce.model.local.room

import androidx.room.*
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDAO {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishList(product: Product)

    @Delete
    suspend fun removeFromWishList(product: Product)

    @Query("DELETE FROM products")
    suspend fun reset()

}