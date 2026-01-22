package com.example.pantallas.ui.biblioteca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Libro
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
    fun agregarLibroAMiBiblioteca(nuevoLibro: Libro, seccion: String) {
        // 1. Obtenemos una copia actual de la biblioteca para no mutar el estado directamente
        val bibliotecaActual = biblioteca

        // 2. Creamos una nueva versión de la biblioteca con el libro añadido en la sección correcta
        val nuevaBiblioteca = when (seccion) {
            "Recomendados" -> {
                bibliotecaActual.copy(
                    librosRecomendados = bibliotecaActual.librosRecomendados + nuevoLibro
                )
            }
            "Últimos libros" -> { // O "Leídos" según cómo lo llames en tu UI
                bibliotecaActual.copy(
                    librosLeidos = bibliotecaActual.librosLeidos + nuevoLibro
                )
            }
            "Futuras lecturas" -> {
                bibliotecaActual.copy(
                    librosFuturasLecturas = bibliotecaActual.librosFuturasLecturas + nuevoLibro
                )
            }
            else -> bibliotecaActual // Si la sección no coincide, no hacemos cambios
        }

        // 3. Actualizamos el estado observable.
        // Al ser un 'mutableStateOf', Compose detectará el cambio y redibujará la pantalla automáticamente.
        biblioteca = nuevaBiblioteca
    }

    private fun cargarBiblioteca() {
        // En una aplicación real, aquí obtendrías los datos de una fuente asíncrona.
        // Aquí solo aseguramos que el estado mutable se inicialice con los datos correctos.
        biblioteca = BibliotecaInicial
    }
}