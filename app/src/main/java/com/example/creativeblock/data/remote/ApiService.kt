package com.example.creativeblock.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // -------------------------
    // CATALOGOS
    // -------------------------
    @GET("api/categorias")
    suspend fun getCategorias(): List<CatalogItem>

    @GET("api/prioridades")
    suspend fun getPrioridades(): List<CatalogItem>

    @GET("api/estados")
    suspend fun getEstados(): List<CatalogItem>

    // -------------------------
    // IDEAS
    // -------------------------
    @GET("api/ideas")
    suspend fun getIdeas(): List<IdeaDto>

    @POST("api/ideas")
    suspend fun createIdea(@Body request: NewIdeaRequest): IdeaDto

    @DELETE("api/ideas/{id}")
    suspend fun deleteIdea(@Path("id") id: Long)

    //  Metodo para editar idea
    @PUT("api/ideas/{id}")
    suspend fun updateIdea(
        @Path("id") id: Long,
        @Body request: NewIdeaRequest
    ): IdeaDto
}