package com.iti.team.ecommerce.model.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iti.team.ecommerce.model.data_classes.Product

@Database(entities = arrayOf(Product::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductsDAO
}