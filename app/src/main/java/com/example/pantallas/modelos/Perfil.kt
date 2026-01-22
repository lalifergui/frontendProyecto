// Archivo: com/example/pantallas/modelos/Perfil.kt

package com.example.pantallas.modelos

import Usuario

data class Perfil(
    val perfil_id: Long=0L,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val ciudad: String,
    val fotoPerfil: String? = null,
    val usuario: Usuario? = null
) {
    // Definimos un objeto Perfil de ejemplo para usarlo en el ViewModel
    companion object {

        // Necesitamos un Usuario de ejemplo para el Perfil de ejemplo
        val UsuarioEjemplo = Usuario(
            usuario_id = 99L,
            email = "carlos95@example.com",
            password = "hashed_password", // Nunca guardes contraseñas reales así
            perfil = null, // Para evitar un bucle de referencia, lo ponemos a null aquí
            biblioteca = null,
            
        )

        val PerfilEjemplo = Perfil(
            perfil_id = 1L,
            nombre = "Carlos",
            apellidos = "García López",
            fechaNacimiento = "1995-10-25",
            ciudad = "Madrid",
            // Asignamos el objeto Usuario
            usuario = UsuarioEjemplo
        )
    }
}