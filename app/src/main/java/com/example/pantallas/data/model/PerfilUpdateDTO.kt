package com.example.pantallas.data.model

data class PerfilUpdateDTO(
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val ciudad: String,
    val fotoPerfil: String?
)