package com.example.pantallas.data.model

data class LibroDTO(
    val id: Long? = null,
    val titulo: String,
    val autor: String,
    val portada: String? = null,
    val categoriaNombre: String? = null // solo guardamos el ID de la categoría
)