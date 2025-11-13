package com.example.creativeblock.model


// Enum que representa la prioridad de una idea creativa
// Las ideas se pueden ordenar por que tan buenas son
// Colores modernos para mejor visibilidad
enum class Prioridad(val displayName: String, val colorValue: Long) {
    BRILLANTE("Brillante", 0xFFFFD700),     // Dorado - Idea excepcional
    PROMETEDORA("Prometedora", 0xFF00E676), // Verde - Tiene mucho potencial
    COMUN("Comun", 0xFF64D8E8),             // Azul claro - Idea normal
    DESCARTE("Descarte", 0xFFFF5252);       // Rojo - Para eliminar luego

    // Funcion para convertir un String a Prioridad
    companion object {
        fun fromString(value: String): Prioridad {
            // Busca la prioridad por nombre, si no encuentra devuelve COMUN por defecto
            return entries.find { it.name == value } ?: COMUN
        }

        // Obtiene todas las prioridades disponibles para mostrar en la interfaz
        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}