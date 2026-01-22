package com.example.pantallas.data.model

data class BibliotecaDTO(
    val recomendados: Set<LibroDTO> = emptySet(),
    val leidos: Set<LibroDTO> = emptySet(),
    val futurasLecturas: Set<LibroDTO> = emptySet()
)