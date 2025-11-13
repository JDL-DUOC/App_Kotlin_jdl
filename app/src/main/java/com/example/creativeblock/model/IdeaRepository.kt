package com.example.creativeblock.model

import com.example.creativeblock.data.Idea
import com.example.creativeblock.data.IdeaDao
import kotlinx.coroutines.flow.Flow

// Clase que maneja la logica de negocio de las ideas
// Es el intermediario entre el ViewModel y la base de datos
class IdeaRepository(
    // Pasamos el IdeaDao para hacer operaciones en la base de datos
    private val ideaDao: IdeaDao
) {

    // Flow que emite la lista de ideas automaticamente cuando cambian
    val allIdeas: Flow<List<Idea>> = ideaDao.getAllIdeas()

    // Inserta una nueva idea en la base de datos
    suspend fun insertIdea(idea: Idea) {
        ideaDao.insertIdea(idea)
    }

    // Actualiza una idea existente
    suspend fun updateIdea(idea: Idea) {
        ideaDao.updateIdea(idea)
    }

    // Elimina una idea
    suspend fun deleteIdea(idea: Idea) {
        ideaDao.deleteIdea(idea)
    }

    // Obtiene una idea por su ID
    suspend fun getIdeaById(id: Int): Idea? {
        return ideaDao.getIdeaById(id)
    }

    // Obtiene ideas filtradas por categoria
    fun getIdeasByCategoria(categoria: String): Flow<List<Idea>> {
        return ideaDao.getIdeasByCategoria(categoria)
    }

    // Obtiene ideas filtradas por prioridad
    fun getIdeasByPrioridad(prioridad: String): Flow<List<Idea>> {
        return ideaDao.getIdeasByPrioridad(prioridad)
    }

    // Obtiene ideas filtradas por estado
    fun getIdeasByEstado(estado: String): Flow<List<Idea>> {
        return ideaDao.getIdeasByEstado(estado)
    }

    // Obtiene el numero total de ideas
    suspend fun getTotalIdeas(): Int {
        return ideaDao.getTotalIdeas()
    }

    // Elimina las ideas marcadas como DESCARTE
    suspend fun deleteIdeasDescarte() {
        ideaDao.deleteIdeasDescarte()
    }
}