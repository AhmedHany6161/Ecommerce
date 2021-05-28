package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainCollections(
    @Json(name = "id")
    val collectionsId: Long?,

    @Json(name = "title")
    val collectionsTitle: String?,

    @Json(name = "image")
    val collectionsImage: MainCollectionsImage?

)
