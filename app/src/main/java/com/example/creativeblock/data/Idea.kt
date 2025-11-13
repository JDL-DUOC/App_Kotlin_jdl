package com.example.creativeblock.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Esta clase representa una idea creativa en la aplicacion
// Es la entidad principal que se guarda en la base de datos
@Entity(tableName = "ideas")
data class Idea(
    // ID unico para cada idea, se genera automaticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Titulo de la idea (obligatorio)
    val titulo: String,

    // Descripcion detallada de la idea (obligatorio)
    val descripcion: String,

    // Categoria de la idea (Escritura, Arte, Musica, etc.)
    val categoria: String,

    // Prioridad de la idea (Brillante, Prometedora, etc.)
    val prioridad: String,

    // Estado actual de la idea (Idea, Desarrollo, Completado)
    val estado: String,

    // Recursos necesarios para realizar la idea
    val recursosNecesarios: String,

    // Fecha cuando se creo la idea (se genera automaticamente)
    val fechaCreacion: Long = System.currentTimeMillis()
)