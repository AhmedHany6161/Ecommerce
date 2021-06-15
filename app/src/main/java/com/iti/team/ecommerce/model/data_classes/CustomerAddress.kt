package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerAddress(
    @Json(name = "id")
    val id: Long? = 0,

    @Json(name = "customer_id")
    val customerId:Long? = 0,

    @Json(name = "address1")
    val address:String? = "",

    @Json(name = "first_name")
    val firstName:String? = "",

    @Json(name = "last_name")
    val lastName:String? = "",

    @Json(name = "zip")
    val zip: String? = "",

    @Json(name = "city")
    val city: String? = ""
)