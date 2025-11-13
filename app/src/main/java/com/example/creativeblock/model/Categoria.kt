package com.example.creativeblock.model



// Enum que representa las categorias de ideas creativas
// Cada idea debe tener una categoria asignada para organizarlas
enum class Categoria(val displayName: String) {
    ESCRITURA("Escritura"),      // Ideas para escribir: cuentos, poemas, etc.
    ARTE("Arte"),                // Ideas artisticas: pintura, dibujo, escultura
    MUSICA("Musica"),            // Ideas musicales: canciones, melodias, ritmos
    NEGOCIOS("Negocios"),        // Ideas de negocios: startups, productos, servicios
    INVENTOS("Inventos");        // Ideas de inventos: dispositivos, tecnologias

    // Funcion para convertir un String a Categoria
    companion object {
        fun fromString(value: String): Categoria {
            // Busca la categoria por nombre, si no encuentra devuelve ESCRITURA por defecto
            return entries.find { it.name == value } ?: ESCRITURA
        }

        // Obtiene todas las categorias disponibles para mostrar en la interfaz
        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}