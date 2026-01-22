package com.example.pantallas.modelos

import Usuario

data class Biblioteca(
    val id: Long,
    val usuario: Usuario?,
    val librosRecomendados: List<Libro> = emptyList(),
    val librosLeidos: List<Libro> = emptyList(),
    val librosFuturasLecturas: List<Libro> = emptyList()
)