package com.example.pantallas.modelos

data class Libro(
    val id: Long,
    val titulo: String,
    val autor: String,
    val portada: String,
    val categoria: Categoria
){
    companion object {
        // --- Recomendados ---
        val Libro1 = Libro(1, "Cien años de soledad", "García Márquez", "url_portada_1", Categoria.Ficcion)
        val Libro2 = Libro(2, "El nombre del viento", "Patrick Rothfuss", "url_portada_2", Categoria.Aventura)
        val Libro3 = Libro(3, "Cosmos", "Carl Sagan", "url_portada_3", Categoria.Ciencia)

        val LibrosRecomendadosEjemplo = listOf(Libro1, Libro2, Libro3)

        // --- Leídos ---
        val LibrosLeidosEjemplo = listOf(
            Libro1.copy(id=4, titulo="Crimen y Castigo", autor="Dostoievski", categoria = Categoria.Ficcion),
            Libro2.copy(id=5, titulo="20,000 Leguas", autor="Julio Verne", categoria = Categoria.Aventura),
            Libro3.copy(id=6, titulo="Sapiens", autor="Yuval N. Harari", categoria = Categoria.Ciencia)
        )

        // --- Futuras Lecturas ---
        val LibrosFuturasEjemplo = listOf(
            Libro1.copy(id=7, titulo="El Quijote", autor="Cervantes", categoria = Categoria.Ficcion),
            Libro2.copy(id=8, titulo="Dune", autor="Frank Herbert", categoria = Categoria.Aventura),
            Libro3.copy(id=9, titulo="Breve Historia del Tiempo", autor="S. Hawking", categoria = Categoria.Ciencia)
        )
    }
}