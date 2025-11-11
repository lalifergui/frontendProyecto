package com.example.pantallas.data.dto

data class BibliotecaDTO(
    val id: Long? = null,
    val usuarioId: Long,
    val librosRecomendadosIds: List<Long> = emptyList(),
    val librosLeidosIds: List<Long> = emptyList(),
    val librosFuturasLecturasIds: List<Long> = emptyList()
)