package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class EditCustomerModel(
    @Json(name = "customer")
    val customer: EditCustomer,

    @Json(name = "error")
    val error: Error? = null,

    )