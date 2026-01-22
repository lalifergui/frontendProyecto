package com.example.pantallas.api

import com.example.pantallas.data.model.BibliotecaDTO
import retrofit2.http.POST
import retrofit2.http.Path

interface BibliotecaApi {

    // 🎯 Estas rutas coinciden con BibliotecaController.java del backend

    @POST("api/bibliotecas/{id}/recomendados/{libroId}")
    suspend fun agregarLibroARecomendados(
        @Path("id") usuarioId: Long,
        @Path("libroId") libroId: Long
    ): BibliotecaDTO

    @POST("api/bibliotecas/{id}/leidos/{libroId}")
    suspend fun agregarLibroALeidos(
        @Path("id") usuarioId: Long,
        @Path("libroId") libroId: Long
    ): BibliotecaDTO

    @POST("api/bibliotecas/{id}/futuras/{libroId}")
    suspend fun agregarLibroAFuturas(
        @Path("id") usuarioId: Long,
        @Path("libroId") libroId: Long
    ): BibliotecaDTO
}