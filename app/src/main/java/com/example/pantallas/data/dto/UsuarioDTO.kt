package com.example.pantallas.data.dto

data class UsuarioDTO(
    val usuarioId: Long,
    val email: String,
    val perfil: PerfilDTO?,
    val bibliotecaId: Long?,
    val usuariosFavoritosIds: List<Long> = emptyList()
)
