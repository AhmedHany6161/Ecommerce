package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Variants(
    @Json(name = "price")
    val price:String?,

    @Json(name = "inventory_quantity")
    val quantity:Int?,

    @Json(name = "requires_shipping")
    val requiresShipping:Boolean?,

    @Json(name = "inventory_management")
    val inventoryManagement:String?,

    @Json(name = "taxable")
    val taxable: Boolean?,

)
