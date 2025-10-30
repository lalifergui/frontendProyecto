package com.example.pantallas.dtos

data class PerfilDTO(
    val perfilId: Long? = null,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String, // o LocalDate si quieres manejarlo como fecha
    val ciudad: String,
    val fotoPerfil: String?,
    val usuarioId: Long
)