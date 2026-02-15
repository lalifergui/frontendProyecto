package com.example.pantallas.api

import com.example.pantallas.data.model.BibliotecaDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface BibliotecaApi {

    // --- OBTENER BIBLIOTECA ---
    @GET("/bibliotecas/usuario/{usuarioId}")
    suspend fun getBiblioteca(@Path("usuarioId") usuarioId: Long): Response<BibliotecaDTO>

    // --- AÑADIR LIBROS (POST) ---
    @POST("/bibliotecas/usuario/{usuarioId}/recomendados/{libroId}")
    suspend fun agregarLibroARecomendados(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @POST("/bibliotecas/usuario/{usuarioId}/leidos/{libroId}")
    suspend fun agregarLibroALeidos(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @POST("/bibliotecas/usuario/{usuarioId}/futuras/{libroId}")
    suspend fun agregarLibroAFuturas(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @DELETE("/bibliotecas/usuario/{usuarioId}/recomendados/{libroId}")
    suspend fun eliminarLibroDeRecomendados(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @DELETE("/bibliotecas/usuario/{usuarioId}/leidos/{libroId}")
    suspend fun eliminarLibroDeLeidos(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>

    @DELETE("/bibliotecas/usuario/{usuarioId}/futuras/{libroId}")
    suspend fun eliminarLibroDeFuturas(@Path("usuarioId") uid: Long, @Path("libroId") lid: Long): Response<BibliotecaDTO>
    @Multipart
    @POST("/usuarios/{id}/foto")
    suspend fun subirFoto(
        @Path("id") usuarioId: Long,
        @Part file: MultipartBody.Part
    ): Response<String>
}