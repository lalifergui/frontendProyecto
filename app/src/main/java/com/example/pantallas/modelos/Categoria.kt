package com.example.pantallas.modelos

data class Categoria(
    val id: Long,
    val nombre: String
){
    companion object {
        val Ficcion = Categoria(1, "Ficción")
        val Aventura = Categoria(2, "Aventura")
        val Ciencia = Categoria(3, "Ciencia")
    }
}