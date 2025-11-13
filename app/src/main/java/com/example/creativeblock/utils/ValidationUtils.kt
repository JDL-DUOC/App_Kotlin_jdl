package com.example.creativeblock.ui.utils

// Objeto que contiene funciones para validar los campos del formulario
// Se usa para verificar que los datos ingresados sean correctos
object ValidationUtils {

    // Valida que el titulo sea correcto
    // Requisitos: no vacio, minimo 3 caracteres, maximo 50 caracteres
    fun isValidTitulo(titulo: String): Boolean {
        // Verifica que no este vacio
        if (titulo.isBlank()) return false

        // Verifica la longitud
        if (titulo.length < 3 || titulo.length > 50) return false

        return true
    }

    // Valida que la descripcion sea correcta
    // Requisitos: no vacia, minimo 10 caracteres, maximo 500 caracteres
    fun isValidDescripcion(descripcion: String): Boolean {
        // Verifica que no este vacia
        if (descripcion.isBlank()) return false

        // Verifica la longitud
        if (descripcion.length < 10 || descripcion.length > 500) return false

        return true
    }

    // Valida que la categoria no este vacia
    fun isValidCategoria(categoria: String): Boolean {
        return categoria.isNotBlank()
    }

    // Valida que la prioridad no este vacia
    fun isValidPrioridad(prioridad: String): Boolean {
        return prioridad.isNotBlank()
    }

    // Valida que el estado no este vacio
    fun isValidEstado(estado: String): Boolean {
        return estado.isNotBlank()
    }

    // Obtiene el mensaje de error para el campo titulo
    fun getTituloErrorMessage(titulo: String): String? {
        return when {
            titulo.isBlank() -> "El titulo es requerido"
            titulo.length < 3 -> "El titulo debe tener al menos 3 caracteres"
            titulo.length > 50 -> "El titulo no puede exceder 50 caracteres"
            else -> null
        }
    }

    // Obtiene el mensaje de error para el campo descripcion
    fun getDescripcionErrorMessage(descripcion: String): String? {
        return when {
            descripcion.isBlank() -> "La descripcion es requerida"
            descripcion.length < 10 -> "La descripcion debe tener al menos 10 caracteres"
            descripcion.length > 500 -> "La descripcion no puede exceder 500 caracteres"
            else -> null
        }
    }

    // Obtiene el mensaje de error para el campo categoria
    fun getCategoriaErrorMessage(categoria: String): String? {
        return when {
            categoria.isBlank() -> "Debe seleccionar una categoria"
            else -> null
        }
    }

    // Obtiene el mensaje de error para el campo prioridad
    fun getPrioridadErrorMessage(prioridad: String): String? {
        return when {
            prioridad.isBlank() -> "Debe seleccionar una prioridad"
            else -> null
        }
    }

    // Obtiene el mensaje de error para el campo estado
    fun getEstadoErrorMessage(estado: String): String? {
        return when {
            estado.isBlank() -> "Debe seleccionar un estado"
            else -> null
        }
    }
}