package com.example.pantallas.data.repository

import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.data.model.LoginRequestDTO
import com.example.pantallas.data.model.UsuarioDTO
import retrofit2.Response

class LoginRepositorio {
    // Usamos 'usuarioApi' definido en RetrofitClient
    private val api = RetrofitClient.usuarioApi

    /**
     * Realiza la llamada al backend.
     * Ahora devuelve un objeto Response para que el ViewModel pueda
     * decidir qué hacer si hay un error 400 o 404.
     */
    suspend fun login(email: String, password: String): Response<UsuarioDTO> {
        val request = LoginRequestDTO(email, password)
        return api.login(request)
    }
}