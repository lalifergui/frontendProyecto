package com.example.pantallas.modelos

import Usuario

data class Perfil(
    val perfil_id: Long = 0L,
    val nombre: String = "Usuario",       // 🎯 Valor por defecto si llega null
    val apellidos: String = "",           // 🎯 Evita el NullPointerException
    val fechaNacimiento: String = "",
    val ciudad: String = "",
    val fotoPerfil: String? = null,
    val usuario: Usuario? = null
) {
    companion object {
        val UsuarioEjemplo = Usuario(
            usuario_id = 99L,
            email = "carlos95@example.com",
            password = "hashed_password",
            perfil = null,
            biblioteca = null
        )

        val PerfilEjemplo = Perfil(
            perfil_id = 1L,
            nombre = "Carlos",
            apellidos = "García López",
            fechaNacimiento = "1995-10-25",
            ciudad = "Madrid",
            fotoPerfil = null,
            usuario = UsuarioEjemplo
        )
    }
}