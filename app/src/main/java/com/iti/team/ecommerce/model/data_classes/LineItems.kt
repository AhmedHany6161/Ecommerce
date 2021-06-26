package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LineItems(
    @Json(name = "variant_id")
    val variantId: Long? = 0,

    @Json(name = "quantity")
    val quantity: Long? = 0,
)
