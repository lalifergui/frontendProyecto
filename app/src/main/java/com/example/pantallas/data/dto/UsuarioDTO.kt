package com.example.pantallas.data.dto

import com.example.pantallas.modelos.Categoria
import java.time.LocalDate

data class UsuarioDTO(
    //val usuarioId: Long,
    val usuario: String="",
    val apellidos: String="",
    val fechaNacimiento: String="",
    val ciudad: String="",
    val email: String="",
    val password: String="",
    //val perfil: PerfilDTO?,

    //val biblioteca: BibliotecaDTO,
    //val categorias: List<CategoriaDTO>=emp(,
    //val usuariosFavoritosIds: List<Long> = emptyList()
)
