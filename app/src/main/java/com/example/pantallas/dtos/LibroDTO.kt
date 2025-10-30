package com.example.pantallas.dtos

data class LibroDTO(
    val id: Long? = null,
    val titulo: String,
    val autor: String,
    val portada: String?,
    val categoriaId: Long? = null // solo guardamos el ID de la categoría
)