package com.example.creativeblock.model


// Enum que representa el estado de desarrollo de una idea
// Muestra en que fase se encuentra la idea creativa
enum class Estado(val displayName: String) {
    IDEA("Idea"),              // Solo es una idea, no se ha empezado
    DESARROLLO("Desarrollo"),  // Se esta trabajando en la idea
    COMPLETADO("Completado");  // La idea esta terminada

    // Funcion para convertir un String a Estado
    companion object {
        fun fromString(value: String): Estado {
            // Busca el estado por nombre, si no encuentra devuelve IDEA por defecto
            return entries.find { it.name == value } ?: IDEA
        }

        // Obtiene todos los estados disponibles para mostrar en la interfaz
        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}