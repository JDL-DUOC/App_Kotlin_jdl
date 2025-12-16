package com.example.creativeblock.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdeaDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "titulo")
    val titulo: String,

    @Json(name = "descripcion")
    val descripcion: String,

    @Json(name = "recursosNecesarios")
    val recursosNecesarios: String,

    @Json(name = "fechaCreacion")
    val fechaCreacion: String,

    @Json(name = "categoria")
    val categoria: String,  // Viene como "ARTE", "ESCRITURA", etc.

    @Json(name = "prioridad")
    val prioridad: String,  // ¿Cómo viene? "ALTA", "MEDIA"?

    @Json(name = "estado")
    val estado: String      // ¿Cómo viene? "PENDIENTE"?
)