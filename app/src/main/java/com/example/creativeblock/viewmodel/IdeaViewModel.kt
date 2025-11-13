package com.example.creativeblock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creativeblock.data.Idea
import com.example.creativeblock.model.IdeaRepository
import com.example.creativeblock.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Clase que representa el estado del formulario para crear ideas
// Guarda todos los datos que el usuario escribe en la pantalla
data class IdeaUiState(
    // Campos del formulario
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val prioridad: String = "",
    val estado: String = "",
    val recursosNecesarios: String = "",

    // Errores de validacion
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val prioridadError: String? = null,
    val estadoError: String? = null,

    // Estado de carga
    val isLoading: Boolean = false,
    val guardadoExitoso: Boolean = false
)

// ViewModel que maneja toda la logica de la aplicacion
// Sobrevive cuando la pantalla gira y se destruye
class IdeaViewModel(
    private val repository: IdeaRepository
) : ViewModel() {

    // Estado privado del formulario (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow(IdeaUiState())

    // Estado publico del formulario (la pantalla solo puede leerlo)
    val uiState: StateFlow<IdeaUiState> = _uiState.asStateFlow()

    // Flow de todas las ideas (se actualiza automaticamente)
    val allIdeas = repository.allIdeas

    // Funcion cuando el usuario escribe el titulo
    fun onTituloChange(titulo: String) {
        _uiState.value = _uiState.value.copy(
            titulo = titulo,
            tituloError = null  // Limpia el error al escribir
        )
    }

    // Funcion cuando el usuario escribe la descripcion
    fun onDescripcionChange(descripcion: String) {
        _uiState.value = _uiState.value.copy(
            descripcion = descripcion,
            descripcionError = null
        )
    }

    // Funcion cuando el usuario selecciona categoria
    fun onCategoriaChange(categoria: String) {
        _uiState.value = _uiState.value.copy(
            categoria = categoria,
            categoriaError = null
        )
    }

    // Funcion cuando el usuario selecciona prioridad
    fun onPrioridadChange(prioridad: String) {
        _uiState.value = _uiState.value.copy(
            prioridad = prioridad,
            prioridadError = null
        )
    }

    // Funcion cuando el usuario selecciona estado
    fun onEstadoChange(estado: String) {
        _uiState.value = _uiState.value.copy(
            estado = estado,
            estadoError = null
        )
    }

    // Funcion cuando el usuario escribe los recursos necesarios
    fun onRecursosChange(recursos: String) {
        _uiState.value = _uiState.value.copy(
            recursosNecesarios = recursos
        )
    }

    // Funcion cuando el usuario presiona guardar
    fun onGuardarClick() {
        // Primero validamos los campos
        if (!validateFields()) {
            return  // Si hay errores, no guarda
        }

        // Inicia operacion asincrona
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                guardadoExitoso = false
            )

            // Crea la nueva idea
            val idea = Idea(
                titulo = _uiState.value.titulo,
                descripcion = _uiState.value.descripcion,
                categoria = _uiState.value.categoria,
                prioridad = _uiState.value.prioridad,
                estado = _uiState.value.estado,
                recursosNecesarios = _uiState.value.recursosNecesarios
            )

            // Guarda en la base de datos
            repository.insertIdea(idea)

            // Indica que se guardo exitosamente
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                guardadoExitoso = true
            )
        }
    }

    // Funcion para validar todos los campos del formulario
    private fun validateFields(): Boolean {
        val currentState = _uiState.value

        // Valida cada campo usando ValidationUtils
        val tituloError = ValidationUtils.getTituloErrorMessage(currentState.titulo)
        val descripcionError = ValidationUtils.getDescripcionErrorMessage(currentState.descripcion)
        val categoriaError = ValidationUtils.getCategoriaErrorMessage(currentState.categoria)
        val prioridadError = ValidationUtils.getPrioridadErrorMessage(currentState.prioridad)
        val estadoError = ValidationUtils.getEstadoErrorMessage(currentState.estado)

        // Actualiza el estado con los errores
        _uiState.value = currentState.copy(
            tituloError = tituloError,
            descripcionError = descripcionError,
            categoriaError = categoriaError,
            prioridadError = prioridadError,
            estadoError = estadoError
        )

        // Retorna true si no hay errores
        return tituloError == null &&
                descripcionError == null &&
                categoriaError == null &&
                prioridadError == null &&
                estadoError == null
    }

    // Funcion para resetear el formulario
    fun resetForm() {
        _uiState.value = IdeaUiState()
    }

    // Funcion para eliminar una idea
    fun deleteIdea(idea: Idea) {
        viewModelScope.launch {
            repository.deleteIdea(idea)
        }
    }

    // Funcion para actualizar una idea
    fun updateIdea(idea: Idea) {
        viewModelScope.launch {
            repository.updateIdea(idea)
        }
    }

    // Funcion para obtener una idea por ID
    suspend fun getIdeaById(id: Int): Idea? {
        return repository.getIdeaById(id)
    }

    // ========== NUEVAS FUNCIONES PARA EDITAR ==========

    // Funcion para cargar una idea existente en el formulario
    fun loadIdeaForEditing(idea: Idea) {
        _uiState.value = IdeaUiState(
            titulo = idea.titulo,
            descripcion = idea.descripcion,
            categoria = idea.categoria,
            prioridad = idea.prioridad,
            estado = idea.estado,
            recursosNecesarios = idea.recursosNecesarios,
            isLoading = false,
            guardadoExitoso = false
        )
    }

    // Funcion para actualizar una idea existente
    fun updateExistingIdea(ideaId: Int) {
        // Primero validamos los campos
        if (!validateFields()) {
            return
        }

        // Inicia operacion asincrona
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                guardadoExitoso = false
            )

            // Crea la idea actualizada
            val ideaActualizada = Idea(
                id = ideaId, // IMPORTANTE: mantener el mismo ID
                titulo = _uiState.value.titulo,
                descripcion = _uiState.value.descripcion,
                categoria = _uiState.value.categoria,
                prioridad = _uiState.value.prioridad,
                estado = _uiState.value.estado,
                recursosNecesarios = _uiState.value.recursosNecesarios,
                fechaCreacion = System.currentTimeMillis() // Mantener fecha original
            )

            // Actualiza en la base de datos
            repository.updateIdea(ideaActualizada)

            // Indica que se actualizo exitosamente
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                guardadoExitoso = true
            )
        }
    }

    // Funcion para saber si estamos en modo edicion
    fun isEditingMode(): Boolean {
        return _uiState.value.titulo.isNotEmpty() &&
                _uiState.value.descripcion.isNotEmpty()
    }
}