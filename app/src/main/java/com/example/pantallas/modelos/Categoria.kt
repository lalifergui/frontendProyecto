package com.example.pantallas.modelos

data class Categoria(
    val id: Long,
    val nombre: String
) {
    companion object {
        val Fantasia = Categoria(1, "Fantasía")
        val CienciaFiccion = Categoria(2, "Ciencia Ficción")
        val Terror = Categoria(3, "Terror")
        val Romance = Categoria(4, "Romance")
        val Misterio = Categoria(5, "Misterio")
        val Policiaca = Categoria(6, "Policiaca")
        val Aventura = Categoria(7, "Aventura")
        val Historia= Categoria(8, "Historia")
        val Biografia = Categoria(9, "Biografía")
        val Autoayuda = Categoria(10, "Autoayuda")
        val Ensayo = Categoria(11, "Ensayo")
        val Divulgacion = Categoria(12, "Divulgacion")
        val Juvenil = Categoria(13, "Juvenil")
        val Infantil = Categoria(14, "Infantil")
        val Poesia = Categoria(15, "Poesia")
        val Comic = Categoria(16, "Comic")
        val Teatro = Categoria(17, "Teatro")
        val Filosofia = Categoria(18, "Filosofía")
        val Espiritualidad = Categoria(19, "Espiritualidad")
        val Thriller = Categoria(20, "Thriller")
        val Ciencia = Categoria(21, "Ciencia")




        val listaCategorias = listOf(
            Fantasia,
            CienciaFiccion,
            Terror,
            Romance,
            Misterio,
            Policiaca,
            Aventura,
            Historia,
            Biografia,
            Autoayuda,
            Ensayo,
            Divulgacion,
            Juvenil,
            Infantil,
            Poesia,
            Comic,
            Teatro,
            Filosofia,
            Espiritualidad,
            Thriller,
            Ciencia
        )
    }
}