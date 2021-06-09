package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddOrderModel(
    @Json(name = "order")
    val order:SendedOrder? = null

)
