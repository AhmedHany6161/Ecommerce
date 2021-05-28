package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "address1")
    val address: String?,

    @Json(name = "city")
    val city: String?,

    @Json(name = "province")
    val province: String?,

    @Json(name = "phone")
    val phone: String?,

    @Json(name = "zip")
    val zip: String?,

    )

