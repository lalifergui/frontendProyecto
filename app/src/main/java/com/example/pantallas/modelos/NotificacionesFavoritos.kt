package com.example.pantallas.modelos

data class NotificacionesFavoritos(
    val id: Long,
    val nombreUsuario: String,
    val mensaje: String, // Ejemplo: "Ha añadido un nuevo libro"
    val fecha: String,
    val leida: Boolean = false
)