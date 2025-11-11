package com.example.pantallas.data.dto

data class LoginResponse(
    val mensaje:String,
    val token: String?,
    val idUsuario: Long?
)