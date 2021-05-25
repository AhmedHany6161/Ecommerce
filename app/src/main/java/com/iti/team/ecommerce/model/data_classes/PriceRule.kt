package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceRule(
    @Json(name = "price_rule")
    val title: Discount?,
)
