package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CustomerAddressModel(
    @Json(name = "customer_address")
    val customerAddress: CustomerAddress? = null,
)
