package com.example.pantallas.api

import com.example.pantallas.data.model.BibliotecaDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BibliotecaApi {

    // --- OBTENER BIBLIOTECA ---
    @GET("api/bibliotecas/usuario/{id}")
    suspend fun getBiblioteca(@Path("id") usuarioId: Long): Response<BibliotecaDTO>

    // --- AÑADIR LIBROS (POST) ---
    @POST("api/bibliotecas/usuario/{id}/recomendados/{libroId}")
    suspend fun agregarLibroARecomendados(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @POST("api/bibliotecas/usuario/{id}/leidos/{libroId}")
    suspend fun agregarLibroALeidos(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @POST("api/bibliotecas/usuario/{id}/futuras/{libroId}")
    suspend fun agregarLibroAFuturas(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    // --- ELIMINAR LIBROS (DELETE) - ¡FALTABA ESTO! ---
    @DELETE("api/bibliotecas/usuario/{id}/recomendados/{libroId}")
    suspend fun eliminarLibroDeRecomendados(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @DELETE("api/bibliotecas/usuario/{id}/leidos/{libroId}")
    suspend fun eliminarLibroDeLeidos(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @DELETE("api/bibliotecas/usuario/{id}/futuras/{libroId}")
    suspend fun eliminarLibroDeFuturas(@Path("id") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>
}