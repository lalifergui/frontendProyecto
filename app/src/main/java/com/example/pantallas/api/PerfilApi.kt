package com.example.pantallas.api

import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.model.PerfilUpdateDTO
import retrofit2.Response
import retrofit2.http.*

interface PerfilApi {
    @GET("perfiles/usuario/{usuarioId}")
    suspend fun getPerfil(@Path("usuarioId") usuarioId: Long): Response<PerfilDTO>

    @PUT("perfiles/usuario/{usuarioId}")
    suspend fun actualizarPerfil(
        @Path("usuarioId") usuarioId: Long,
        @Body dto: PerfilUpdateDTO
    ): Response<PerfilDTO>
}