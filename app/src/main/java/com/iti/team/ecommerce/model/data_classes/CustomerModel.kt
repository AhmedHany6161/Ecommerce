package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerModel (
    @Json(name = "customer")
    val customer: Customer?,

    @Json(name = "error")
    val error: Error? = null,

)

