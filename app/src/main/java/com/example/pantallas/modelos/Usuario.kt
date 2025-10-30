package com.example.pantallas.modelos

data class Usuario(
    val usuario_id: Long,
    val email: String,
    val password: String,
    val perfil: Perfil?,
    val biblioteca: Biblioteca?,
    val categorias: List<Categoria> = emptyList(),
    val usuariosFavoritos: List<Usuario> = emptyList()
)
