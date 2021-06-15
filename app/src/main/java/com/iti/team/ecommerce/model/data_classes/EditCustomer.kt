package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class EditCustomer(
    @Json(name = "id")
    val customerId: Long? = 0,

    @Json(name = "email")
    val email: String?,


    @Json(name = "phone")
    val phone: String? = "",


    @Json(name = "first_name")
    val firstName: String? = "",

    @Json(name = "last_name")
    val lastName: String? = "",

)