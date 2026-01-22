package com.example.pantallas.data.model

data class LoginRequestDTO(
    val email: String, //Cambiado de 'usuario' a 'email' para que coincida con el backend
    val password: String
)