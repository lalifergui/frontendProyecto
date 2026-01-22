package com.example.pantallas.data.model

data class PerfilDTO(
    val perfilId: Long? = null,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String, // El backend envía LocalDate, Retrofit lo leerá como String
    val ciudad: String,
    val fotoPerfil: String?,
    val usuario: UsuarioDTO? = null
)