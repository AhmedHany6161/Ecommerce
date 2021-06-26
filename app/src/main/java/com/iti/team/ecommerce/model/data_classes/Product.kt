package com.iti.team.ecommerce.model.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "products")
@JsonClass(generateAdapter = true)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val image: String,
    val brand: String,
    val price: String,
    var count: Int = 0,
    var inWish: Boolean = false,
    var inCart: Boolean = false,
    var variant_id:Long? =0
)
