package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Order(

    @Json(name = "id")
    val customerId: Long? = 0,

    @Json(name = "app_id")
    val orderNumber: Long? = 0,
    @Json(name = "created_at")
    val createdAt: String? = "",

    @Json(name = "current_subtotal_price")
    val finalPrice: String? = "",

    @Json(name = "current_total_discounts")
    val totalDiscount: String? = "",
)
