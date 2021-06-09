package com.iti.team.ecommerce.model.local.room

import androidx.room.*
import com.iti.team.ecommerce.model.data_classes.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDAO {
    @Query("SELECT * FROM products where inWish = 1")
    fun getWishList(): Flow<List<Product>>

    @Query("SELECT id FROM products where inWish = 1")
    fun getAllWishListIds(): Flow<List<Long>>

    @Query("SELECT * FROM products where inCart = 1")
    fun getCart(): Flow<List<Product>>

    @Query("SELECT * FROM products where id = :id")
    suspend fun getById(id: Long): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("DELETE FROM products where id = :id")
    suspend fun remove(id: Long)

    @Query("DELETE FROM products")
    suspend fun reset()

}