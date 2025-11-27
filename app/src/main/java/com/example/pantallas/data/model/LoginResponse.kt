package com.example.pantallas.data.model

data class LoginResponse(
    val mensaje:String,
    val token: String?,
    val idUsuario: Long?
)