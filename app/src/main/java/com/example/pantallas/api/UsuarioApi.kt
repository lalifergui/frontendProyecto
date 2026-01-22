package com.example.pantallas.api

import com.example.pantallas.data.model.UsuarioDTO
import com.example.pantallas.data.model.UsuarioRegisterDTO
import com.example.pantallas.data.model.LoginRequestDTO
import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.model.BibliotecaDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}