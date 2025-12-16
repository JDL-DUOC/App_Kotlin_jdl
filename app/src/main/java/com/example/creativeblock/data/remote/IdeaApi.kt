package com.example.creativeblock.data.remote

import retrofit2.http.*

interface IdeaApi {

    @GET("api/ideas")
    suspend fun getIdeas(): List<IdeaDto>

    @POST("api/ideas")
    suspend fun createIdea(
        @Body request: NewIdeaRequest
    ): IdeaDto

    @DELETE("api/ideas/{id}")
    suspend fun deleteIdea(
        @Path("id") id: Long
    )

    //  Metodo para editar idea
    @PUT("api/ideas/{id}")
    suspend fun updateIdea(
        @Path("id") id: Long,
        @Body request: NewIdeaRequest
    ): IdeaDto
}