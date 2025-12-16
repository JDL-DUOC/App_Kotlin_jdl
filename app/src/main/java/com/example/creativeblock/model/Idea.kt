package com.example.creativeblock.model



data class Idea(
    val id: Long = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val prioridad: String = "",
    val estado: String = "",
    val recursosNecesarios: String = "",
    val fechaCreacion: String = ""
)
