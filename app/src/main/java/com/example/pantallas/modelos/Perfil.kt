package com.example.pantallas.modelos

import Usuario

// Asegúrate de que la clase Usuario también esté en este paquete.
// Si no, necesitarás: import com.example.pantallas.modelos.Usuario

data class Perfil(
    val perfil_id: Long = 0L,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val ciudad: String,
    val fotoPerfil: String? = null, // Esta es la variable clave para la foto
    val usuario: Usuario? = null
) {
    // Objeto de ejemplo para pruebas y ViewModels
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
            fotoPerfil = null, // Añadido explícitamente para claridad
            usuario = UsuarioEjemplo
        )
    }
}