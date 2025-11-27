package com.example.pantallas.modelos

data class Categoria(
    val id: Long,
    val nombre: String
){
    companion object {
        val Ficcion = Categoria(1, "Ficción")
        val Aventura = Categoria(2, "Aventura")
        val Ciencia = Categoria(3, "Ciencia")
        val Poesia = Categoria(4, "Poesia")
        val Fantasia = Categoria(5, "Fantasia")
        val Policiaca= Categoria(6,"Novela Policiaca")

        val listaCategorias=listOf(Ficcion,Aventura,Ciencia,Poesia,Fantasia,Policiaca)
    }
}