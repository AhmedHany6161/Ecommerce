package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json

data class CustomerLoginModel(
    @Json(name = "customers")
    val customer: List<Customer?>,

    @Json(name = "error")
    val error: Error? = null,
)
