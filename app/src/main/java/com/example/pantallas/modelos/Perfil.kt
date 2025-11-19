// Archivo: com/example/pantallas/modelos/Perfil.kt

package com.example.pantallas.modelos

data class Perfil(
    val perfil_id: Long,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val ciudad: String,
    val fotoPerfil: String? = null,
    // ¡CORRECCIÓN AQUÍ! Usamos el objeto Usuario.
    val usuario: Usuario
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
            categorias = emptyList(),
            usuariosFavoritos = emptyList()
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