package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DiscountCodes(
    @Json(name = "code")
    val code: String? = "",

    @Json(name = "amount")
    val amount: String? = "10.0",

    @Json(name = "type")
    val type: String? = "percentage",
)
