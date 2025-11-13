package com.example.creativeblock.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Interface que define las operaciones de base de datos para las ideas
// Room se encarga de implementar estas funciones automaticamente
@Dao
interface IdeaDao {

    // Inserta una nueva idea en la base de datos
    @Insert
    suspend fun insertIdea(idea: Idea)

    // Actualiza una idea existente
    @Update
    suspend fun updateIdea(idea: Idea)

    // Elimina una idea
    @Delete
    suspend fun deleteIdea(idea: Idea)

    // Obtiene todas las ideas ordenadas por fecha de creacion (mas recientes primero)
    @Query("SELECT * FROM ideas ORDER BY fechaCreacion DESC")
    fun getAllIdeas(): Flow<List<Idea>>

    // Obtiene una idea especifica por su ID
    @Query("SELECT * FROM ideas WHERE id = :id")
    suspend fun getIdeaById(id: Int): Idea?

    // Obtiene ideas filtradas por categoria
    @Query("SELECT * FROM ideas WHERE categoria = :categoria ORDER BY fechaCreacion DESC")
    fun getIdeasByCategoria(categoria: String): Flow<List<Idea>>

    // Obtiene ideas filtradas por prioridad
    @Query("SELECT * FROM ideas WHERE prioridad = :prioridad ORDER BY fechaCreacion DESC")
    fun getIdeasByPrioridad(prioridad: String): Flow<List<Idea>>

    // Obtiene ideas filtradas por estado
    @Query("SELECT * FROM ideas WHERE estado = :estado ORDER BY fechaCreacion DESC")
    fun getIdeasByEstado(estado: String): Flow<List<Idea>>

    // Obtiene el numero total de ideas
    @Query("SELECT COUNT(*) FROM ideas")
    suspend fun getTotalIdeas(): Int

    // Elimina todas las ideas marcadas como DESCARTE
    @Query("DELETE FROM ideas WHERE prioridad = 'DESCARTE'")
    suspend fun deleteIdeasDescarte()
}