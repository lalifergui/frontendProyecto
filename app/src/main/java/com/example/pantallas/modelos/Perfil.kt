package com.example.pantallas.modelos

data class Perfil(
    val perfil_id: Long,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String, // ISO "yyyy-MM-dd"
    val ciudad: String,
    val fotoPerfil: String?,
    val usuario: Usuario?
)
