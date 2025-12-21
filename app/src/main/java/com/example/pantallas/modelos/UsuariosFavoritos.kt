package com.example.pantallas.modelos

data class UsuariosFavoritos(
    val id: Long,
    val nombre: String,
    val perfil: Perfil,
    val ultimaActualizacion: String
)