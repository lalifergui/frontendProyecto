package com.example.pantallas.ui.principal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Categoria
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrincipalViewModel : ViewModel() {

    // --- ESTADO DE LA VISTA ---
    var libroActual: Libro? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(true)
        private set

    // ID de la biblioteca que estamos viendo
    var bibliotecaActualId: Long by mutableStateOf(1L)
        private set

    // 🎯 NUEVO: Estado para la categoría seleccionada
    var categoriaSeleccionada: Categoria? by mutableStateOf(null)
        private set

    init {
        cargarSiguienteLibro()
    }

    // --- LÓGICA DE FILTRADO ---

    /**
     * Se llama desde el Dropdown de la UI para aplicar el filtro
     */
    fun filtrarPorCategoria(categoria: Categoria) {
        categoriaSeleccionada = categoria
        // Al cambiar el filtro, reiniciamos la búsqueda desde la primera biblioteca disponible con esa categoría
        bibliotecaActualId = 1L
        cargarSiguienteLibro()
    }

    // --- LÓGICA DE SWIPE ---

    fun likeLibro(libro: Libro, usuarioTargetId: Long) {
        viewModelScope.launch {
            println("Guardando usuario $usuarioTargetId en favoritos.")
            transicionarASiguienteBiblioteca()
            cargarSiguienteLibro()
        }
    }

    fun dislikeLibro() {
        viewModelScope.launch {
            println("Libro descartado. Pasando a la siguiente biblioteca.")
            transicionarASiguienteBiblioteca()
            cargarSiguienteLibro()
        }
    }

    private fun transicionarASiguienteBiblioteca() {
        bibliotecaActualId += 1
    }

    /**
     * Carga el próximo libro respetando el filtro de categoría si existe.
     */
    private fun cargarSiguienteLibro() {
        viewModelScope.launch {
            isLoading = true

            // Simulación de delay de red
            delay(500)

            // 🎯 LÓGICA DE CATEGORÍA:
            // Si hay una categoría seleccionada, el libro generado la usará.
            // En un caso real, aquí harías: repositorio.getLibro(bibliotecaId, categoriaId)

            val categoriaParaElLibro = categoriaSeleccionada ?: Categoria(id = 0, nombre = "General")

            val newId = bibliotecaActualId * 100 + (0..10).random()

            libroActual = Libro(
                id = newId.toLong(),
                titulo = "Libro de ${categoriaParaElLibro.nombre} (User ${bibliotecaActualId})",
                autor = "Autor de ${categoriaParaElLibro.nombre}",
                portada = "",
                categoria = categoriaParaElLibro
            )

            isLoading = false
            println("Cargado libro de categoría: ${categoriaParaElLibro.nombre}")
        }
    }
}