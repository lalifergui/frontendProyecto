package com.example.pantallas.data.model

data class UsuarioDTO(
    val id: Long,
    val email: String,

    //
    val nombre: String? = null,
    val apellidos: String? = null,
    val ciudad: String? = null,
    val fechaNacimiento: String? = null,
    val perfilId: Long? = null,
    val fotoPerfil: String? = null
)