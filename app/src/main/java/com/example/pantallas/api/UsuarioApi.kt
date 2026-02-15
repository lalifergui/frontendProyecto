package com.example.pantallas.api

import com.example.pantallas.data.model.UsuarioDTO
import com.example.pantallas.data.model.UsuarioRegisterDTO
import com.example.pantallas.data.model.LoginRequestDTO
import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.model.BibliotecaDTO
import com.example.pantallas.data.model.NotificacionesFavoritos
import com.example.pantallas.data.model.UsuarioSwipeDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UsuarioApi {

    // 1. Login: Coincide con @PostMapping("/login") en el backend
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequestDTO): retrofit2.Response<UsuarioDTO>

    // 2. Registro: Coincide con @PostMapping("/register") [cite: 36]
    @POST("usuarios/register")
    suspend fun registrar(@Body request: UsuarioRegisterDTO): retrofit2.Response<UsuarioDTO>

    // 3. Obtener Perfil: Coincide con @GetMapping("/{id}/perfil") [cite: 37]
    @GET("usuarios/{id}/perfil")
    suspend fun getPerfil(@Path("id") id: Long): retrofit2.Response<PerfilDTO>

    // 4. Obtener Biblioteca: Coincide con @GetMapping("/{id}/biblioteca") [cite: 37]
    @GET("usuarios/{id}/biblioteca")
    suspend fun getBiblioteca(@Path("id") id: Long) : retrofit2.Response<BibliotecaDTO>
    @POST("usuarios/{id}/favoritos/{favoritoId}")
    suspend fun addFavorito(
        @Path("id") id: Long,
        @Path("favoritoId") favoritoId: Long
    ): Response<Void>
    @Multipart
    @POST("usuarios/{id}/upload-foto")
    suspend fun subirFotoPerfil(
        @Path("id") id: Long,
        @Part file: MultipartBody.Part
    ): Response<String>
    @GET("usuarios")
    suspend fun getAllUsuarios(): Response<List<UsuarioDTO>>
    // Añade esto a tu interfaz UsuarioApi
    @DELETE("usuarios/{id}/favoritos/{favoritoId}")
    suspend fun eliminarFavorito(
        @Path("id") id: Long,
        @Path("favoritoId") favoritoId: Long
    ): Response<Void>
    @GET("usuarios/{id}/favoritos")
    suspend fun getFavoritos(@Path("id") id: Long): Response<List<UsuarioSwipeDTO>>
    @GET("usuarios/{id}/favoritos/notificaciones")
    suspend fun getNotificacionesFavoritos(@Path("id") id: Long): Response<List<NotificacionesFavoritos>>
}