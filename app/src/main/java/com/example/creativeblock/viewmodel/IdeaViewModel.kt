package com.example.creativeblock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creativeblock.data.IdeaRepository
import com.example.creativeblock.data.remote.CatalogItem
import com.example.creativeblock.data.remote.IdeaDto
import com.example.creativeblock.data.remote.NewIdeaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IdeaViewModel(
    private val repository: IdeaRepository
) : ViewModel() {

    // IDEAS
    private val _ideas = MutableStateFlow<List<IdeaDto>>(emptyList())
    val ideas: StateFlow<List<IdeaDto>> = _ideas

    // CATÁLOGOS
    private val _categorias = MutableStateFlow<List<CatalogItem>>(emptyList())
    val categorias: StateFlow<List<CatalogItem>> = _categorias

    private val _prioridades = MutableStateFlow<List<CatalogItem>>(emptyList())
    val prioridades: StateFlow<List<CatalogItem>> = _prioridades

    private val _estados = MutableStateFlow<List<CatalogItem>>(emptyList())
    val estados: StateFlow<List<CatalogItem>> = _estados

    // ESTADOS DE CARGA Y ERROR
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // CARGAR TODOS LOS DATOS
    fun loadAllData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                // Cargar en paralelo
                val ideasDeferred = launch { _ideas.value = repository.getIdeas() }
                val categoriasDeferred = launch { _categorias.value = repository.getCategorias() }
                val prioridadesDeferred = launch { _prioridades.value = repository.getPrioridades() }
                val estadosDeferred = launch { _estados.value = repository.getEstados() }

                // Esperar a que todo termine
                ideasDeferred.join()
                categoriasDeferred.join()
                prioridadesDeferred.join()
                estadosDeferred.join()

            } catch (e: Exception) {
                _error.value = "Error al cargar datos: ${e.message}"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    // CARGAR SOLO IDEAS (para HomeScreen)
    fun loadIdeas() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _ideas.value = repository.getIdeas()
            } catch (e: Exception) {
                _error.value = "Error al cargar ideas: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // CREAR NUEVA IDEA
    fun addIdea(
        titulo: String,
        descripcion: String,
        categoria: Int,
        prioridad: Int,
        estado: Int,
        recursos: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.createIdea(
                    NewIdeaRequest(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        prioridad = prioridad,
                        estado = estado,
                        recursosNecesarios = recursos
                    )
                )

                // Recargar ideas después de crear
                _ideas.value = repository.getIdeas()

            } catch (e: Exception) {
                _error.value = "Error al crear idea: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // NUEVO: EDITAR IDEA EXISTENTE
    fun updateIdea(
        id: Long,
        titulo: String,
        descripcion: String,
        categoria: Int,
        prioridad: Int,
        estado: Int,
        recursos: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.updateIdea(
                    id,
                    NewIdeaRequest(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        prioridad = prioridad,
                        estado = estado,
                        recursosNecesarios = recursos
                    )
                )

                // Recargar ideas después de editar
                _ideas.value = repository.getIdeas()

            } catch (e: Exception) {
                _error.value = "Error al actualizar idea: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // ELIMINAR IDEA
    fun deleteIdea(id: Long) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.deleteIdea(id)
                _ideas.value = repository.getIdeas()

            } catch (e: Exception) {
                _error.value = "Error al eliminar idea: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // LIMPIAR ERROR
    fun clearError() {
        _error.value = null
    }
}