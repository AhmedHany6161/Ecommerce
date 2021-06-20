package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderDetails(
    @Json(name = "fulfillable_quantity")
    val quantity: Long? = 0,
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "price")
    val price: String? = "",
    @Json(name = "vendor")
    val vendor: String? = "",
    @Json(name = "product_id")
    val id: Long = 0
)

