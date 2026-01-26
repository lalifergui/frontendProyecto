package com.example.pantallas.ui.biblioteca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Libro

class BibliotecaViewModel : ViewModel() {

    // 1. INICIALIZAR VACÍA (Para el usuario nuevo)
    // En el futuro, aquí cargarías desde el Backend si el usuario ya tiene libros.
    private val BibliotecaVacia = Biblioteca(
        id = 0L,
        usuario = null,
        librosRecomendados = emptyList(),
        librosLeidos = emptyList(),
        librosFuturasLecturas = emptyList()
    )

    var biblioteca: Biblioteca by mutableStateOf(BibliotecaVacia)
        private set

    // 2. PROPIEDAD COMPUTADA PARA LA ALERTA
    // Devuelve true si hay al menos un libro en cualquier sección
    val tieneLibros: Boolean
        get() = biblioteca.librosRecomendados.isNotEmpty() ||
                biblioteca.librosLeidos.isNotEmpty() ||
                biblioteca.librosFuturasLecturas.isNotEmpty()

    // 3. FUNCIÓN PARA AGREGAR
    fun agregarLibroAMiBiblioteca(nuevoLibro: Libro, seccion: String) {
        val bibliotecaActual = biblioteca

        // Evitar duplicados si lo deseas (opcional)
        // if (bibliotecaActual.librosRecomendados.contains(nuevoLibro)) return

        val nuevaBiblioteca = when (seccion) {
            "Recomendados" -> bibliotecaActual.copy(librosRecomendados = bibliotecaActual.librosRecomendados + nuevoLibro)
            "Últimos libros" -> bibliotecaActual.copy(librosLeidos = bibliotecaActual.librosLeidos + nuevoLibro)
            "Futuras lecturas" -> bibliotecaActual.copy(librosFuturasLecturas = bibliotecaActual.librosFuturasLecturas + nuevoLibro)
            else -> bibliotecaActual
        }
        biblioteca = nuevaBiblioteca
    }

    // 4. FUNCIÓN PARA ELIMINAR
    fun eliminarLibro(libro: Libro, seccion: String) {
        val bibliotecaActual = biblioteca
        val nuevaBiblioteca = when (seccion) {
            "Recomendados" -> bibliotecaActual.copy(librosRecomendados = bibliotecaActual.librosRecomendados - libro)
            "Últimos libros" -> bibliotecaActual.copy(librosLeidos = bibliotecaActual.librosLeidos - libro)
            "Futuras lecturas" -> bibliotecaActual.copy(librosFuturasLecturas = bibliotecaActual.librosFuturasLecturas - libro)
            else -> bibliotecaActual
        }
        biblioteca = nuevaBiblioteca
    }
}