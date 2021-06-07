package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Customer(
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

    @Json(name = "orders_count")
    val ordersCount: Int = 0,

    @Json(name = "state")
    val state: String? = "",

    @Json(name = "currency")
    val currency: String? = "EGP",

    @Json(name = "note")
    val note: String? = "",

    @Json(name = "total_spent")
    val totalSpent: String? = "",

    @Json(name = "addresses")
    val addresses: List<Address>? = listOf(),

    @Json(name = "password")
    val password: String? = "",

    @Json(name = "password_confirmation")
    val passwordConfirmation: String? = "",
)
