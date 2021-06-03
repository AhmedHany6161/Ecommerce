package com.iti.team.ecommerce.model.local.room

import androidx.room.*
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDAO {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT id FROM products")
    fun getAllIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishList(product: Product)

    @Query("DELETE FROM products where id = :id")
    suspend fun removeFromWishList(id:Long)

    @Query("DELETE FROM products")
    suspend fun reset()

}