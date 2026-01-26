package com.example.pantallas.ui.biblioteca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.data.model.BibliotecaDTO
import com.example.pantallas.data.model.LibroDTO
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro
import kotlinx.coroutines.launch

// Clase auxiliar para saber qué cambio hay que enviar al servidor
data class CambioPendiente(
    val tipoAccion: String, // "ADD" o "DELETE"
    val seccion: String,    // "Recomendados", "Últimos libros", "Futuras lecturas"
    val libroId: Long
)

class BibliotecaViewModel : ViewModel() {

    // Instancia de tu servicio API
    private val apiService = RetrofitClient.bibliotecaApi

    // Estado inicial vacío
    private val BibliotecaVacia = Biblioteca(
        id = 0L,
        usuario = null,
        librosRecomendados = emptyList(),
        librosLeidos = emptyList(),
        librosFuturasLecturas = emptyList()
    )

    var biblioteca: Biblioteca by mutableStateOf(BibliotecaVacia)
        private set

    // Cola de cambios pendientes
    private val cambiosPendientes = mutableListOf<CambioPendiente>()

    // Estado para bloquear el botón mientras guarda
    var guardando by mutableStateOf(false)

    val tieneLibros: Boolean
        get() = biblioteca.librosRecomendados.isNotEmpty() ||
                biblioteca.librosLeidos.isNotEmpty() ||
                biblioteca.librosFuturasLecturas.isNotEmpty()

    // --- 1. CARGAR DATOS REALES DEL SERVIDOR ---
    fun cargarBibliotecaReal(usuarioId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.getBiblioteca(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val dto = response.body()!!
                    // Mapeamos el DTO (del servidor) al Modelo (de la UI)
                    biblioteca = mapearDTOaModelo(dto)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- 2. AGREGAR LIBRO (Visual + Cola) ---
    fun agregarLibroAMiBiblioteca(nuevoLibro: Libro, seccion: String) {
        val actual = biblioteca

        // Actualizamos visualmente
        biblioteca = when (seccion) {
            "Recomendados" -> actual.copy(librosRecomendados = actual.librosRecomendados + nuevoLibro)
            "Últimos libros" -> actual.copy(librosLeidos = actual.librosLeidos + nuevoLibro)
            "Futuras lecturas" -> actual.copy(librosFuturasLecturas = actual.librosFuturasLecturas + nuevoLibro)
            else -> actual
        }

        // Añadimos a la cola para guardar luego
        cambiosPendientes.add(CambioPendiente("ADD", seccion, nuevoLibro.id))
    }

    // --- 3. ELIMINAR LIBRO (Visual + Cola) ---
    fun eliminarLibro(libro: Libro, seccion: String) {
        val actual = biblioteca

        // Actualizamos visualmente
        biblioteca = when (seccion) {
            "Recomendados" -> actual.copy(librosRecomendados = actual.librosRecomendados - libro)
            "Últimos libros" -> actual.copy(librosLeidos = actual.librosLeidos - libro)
            "Futuras lecturas" -> actual.copy(librosFuturasLecturas = actual.librosFuturasLecturas - libro)
            else -> actual
        }

        // Añadimos a la cola para borrar luego
        cambiosPendientes.add(CambioPendiente("DELETE", seccion, libro.id))
    }

    // --- 4. GUARDAR CAMBIOS EN EL BACKEND ---
    fun guardarCambiosEnServidor(usuarioId: Long, onTerminado: () -> Unit) {
        if (cambiosPendientes.isEmpty()) {
            onTerminado()
            return
        }

        guardando = true
        viewModelScope.launch {
            try {
                // Recorremos la lista de pendientes y llamamos a la API
                cambiosPendientes.forEach { cambio ->
                    if (cambio.tipoAccion == "ADD") {
                        when (cambio.seccion) {
                            "Recomendados" -> apiService.agregarLibroARecomendados(usuarioId, cambio.libroId)
                            "Últimos libros" -> apiService.agregarLibroALeidos(usuarioId, cambio.libroId)
                            "Futuras lecturas" -> apiService.agregarLibroAFuturas(usuarioId, cambio.libroId)
                        }
                    } else if (cambio.tipoAccion == "DELETE") {
                        when (cambio.seccion) {
                            "Recomendados" -> apiService.eliminarLibroDeRecomendados(usuarioId, cambio.libroId)
                            "Últimos libros" -> apiService.eliminarLibroDeLeidos(usuarioId, cambio.libroId)
                            "Futuras lecturas" -> apiService.eliminarLibroDeFuturas(usuarioId, cambio.libroId)
                        }
                    }
                }
                cambiosPendientes.clear()
                onTerminado() // Éxito
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                guardando = false
            }
        }
    }

    // Función auxiliar para convertir lo que llega del servidor a tu objeto Libro
    private fun mapearDTOaModelo(dto: BibliotecaDTO): Biblioteca {
        return Biblioteca(
            id = 0, // El ID de biblioteca no es crítico en la UI
            usuario = null,
            librosRecomendados = dto.recomendados.map { convertirLibro(it) },
            librosLeidos = dto.leidos.map { convertirLibro(it) },
            librosFuturasLecturas = dto.futurasLecturas.map { convertirLibro(it) }
        )
    }

    private fun convertirLibro(dto: LibroDTO): Libro {
        return Libro(
            id = dto.id ?: 0L,
            titulo = dto.titulo,
            autor = dto.autor,
            portada = dto.portada ?: "",
            categoria = Categoria(0, dto.categoriaNombre ?: "General")
        )
    }
}