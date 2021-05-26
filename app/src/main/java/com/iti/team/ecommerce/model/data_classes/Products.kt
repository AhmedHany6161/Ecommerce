package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Products(
    @Json(name = "id")
    val productId: Long?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "body_html")
    val description:String ?,

    @Json(name = "status")
    val status: String?,

    @Json(name = "vendor")
    val vendor: String?,

    @Json(name = "variants")
    val variants: List<Variants?> = listOf()



)