package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerAddress(
    @Json(name = "id")
    val id: String? = "",

    @Json(name = "customer_id")
    val customerId: String? = "",
)