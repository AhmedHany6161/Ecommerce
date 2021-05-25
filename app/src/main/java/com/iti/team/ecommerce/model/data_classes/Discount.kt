package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Discount(
    @Json(name = "title")
    val title: String?,

    @Json(name = "target_type")
    val targetType:String?,

    @Json(name = "target_selection")
    val targetSelection:String?,

    @Json(name = "allocation_method")
    val allocationMethod:String?,

    @Json(name = "value_type")
    val valueType:String?,

    @Json(name = "value")
    val value:String?,

    @Json(name = "customer_selection")
    val customerSelection:String?,

    @Json(name = "starts_at")
    val startsAt:String?,



)
