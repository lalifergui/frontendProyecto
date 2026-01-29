package com.example.pantallas.data.model

// En com.example.pantallas.modelos
data class NotificacionesFavoritos(
    val id: Long,
    val nombreUsuario: String,
    val mensaje: String, // Ejemplo: "ha añadido un nuevo libro"
    val fecha: String
)