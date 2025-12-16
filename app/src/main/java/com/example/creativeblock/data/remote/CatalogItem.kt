package com.example.creativeblock.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatalogItem(
    @Json(name = "id")
    val id: Int,

    @Json(name = "nombre")
    val nombre: String
)