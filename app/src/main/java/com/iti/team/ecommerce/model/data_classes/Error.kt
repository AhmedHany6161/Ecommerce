package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Error(
    @Json(name = "email")
    val email: List<String>? = listOf(),

    @Json(name = "phone")
    val phone: List<String>? = listOf(),
)
