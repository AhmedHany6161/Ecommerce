package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Transactions(
    @Json(name = "kind")
    val kind: String? = "",

    @Json(name = "status")
    val status: String? = "",

    @Json(name = "amount")
    val amount: Double? = 0.0,
)
