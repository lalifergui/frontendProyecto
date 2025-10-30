package com.example.pantallas.modelos

data class Libro(
    val id: Long,
    val titulo: String,
    val autor: String,
    val portada: String,
    val categoria: Categoria
)