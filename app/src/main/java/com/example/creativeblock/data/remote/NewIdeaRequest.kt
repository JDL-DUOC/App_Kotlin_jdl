package com.example.creativeblock.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewIdeaRequest(
    @Json(name = "titulo")
    val titulo: String,

    @Json(name = "descripcion")
    val descripcion: String,

    @Json(name = "categoria")
    val categoria: Int,

    @Json(name = "prioridad")
    val prioridad: Int,

    @Json(name = "estado")
    val estado: Int,

    @Json(name = "recursosNecesarios")
    val recursosNecesarios: String
)