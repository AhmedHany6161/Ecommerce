package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainCategories(
    @Json(name = "custom_collections")
    val collections:List<MainCollections>? = listOf(),
)
