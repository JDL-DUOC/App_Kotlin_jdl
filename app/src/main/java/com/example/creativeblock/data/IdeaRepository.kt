package com.example.creativeblock.data

import com.example.creativeblock.data.remote.ApiService
import com.example.creativeblock.data.remote.IdeaApi
import com.example.creativeblock.data.remote.CatalogItem
import com.example.creativeblock.data.remote.IdeaDto
import com.example.creativeblock.data.remote.NewIdeaRequest

class IdeaRepository(
    private val ideaApi: IdeaApi,
    private val apiService: ApiService
) {

    // IDEAS
    suspend fun getIdeas(): List<IdeaDto> =
        ideaApi.getIdeas()

    suspend fun createIdea(request: NewIdeaRequest): IdeaDto =
        ideaApi.createIdea(request)

    suspend fun deleteIdea(id: Long) =
        ideaApi.deleteIdea(id)

    // Metodo para editar idea
    suspend fun updateIdea(id: Long, request: NewIdeaRequest): IdeaDto =
        ideaApi.updateIdea(id, request)

    // CATÁLOGOS
    suspend fun getCategorias(): List<CatalogItem> =
        apiService.getCategorias()

    suspend fun getPrioridades(): List<CatalogItem> =
        apiService.getPrioridades()

    suspend fun getEstados(): List<CatalogItem> =
        apiService.getEstados()
}