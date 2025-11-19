package com.example.pantallas.ui.biblioteca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Libro.Companion.LibrosFuturasEjemplo
import com.example.pantallas.modelos.Libro.Companion.LibrosLeidosEjemplo
import com.example.pantallas.modelos.Libro.Companion.LibrosRecomendadosEjemplo

class BibliotecaViewModel : ViewModel() {

    // Define la biblioteca de ejemplo aquí mismo usando las listas del modelo Libro
    private val BibliotecaInicial = Biblioteca(
        id = 1L,
        usuario = null,
        librosRecomendados = LibrosRecomendadosEjemplo,
        librosLeidos = LibrosLeidosEjemplo,
        librosFuturasLecturas = LibrosFuturasEjemplo
    )

    var biblioteca: Biblioteca by mutableStateOf(BibliotecaInicial)
        private set

    init {
        // Llama a cargarBiblioteca para inicializar el estado
        cargarBiblioteca()
    }

    private fun cargarBiblioteca() {
        // En una aplicación real, aquí obtendrías los datos de una fuente asíncrona.
        // Aquí solo aseguramos que el estado mutable se inicialice con los datos correctos.
        biblioteca = BibliotecaInicial
    }
}