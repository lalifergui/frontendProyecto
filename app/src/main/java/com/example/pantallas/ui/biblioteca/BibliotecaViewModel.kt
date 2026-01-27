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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CambioPendiente(
    val tipoAccion: String,
    val seccion: String,
    val libroId: Long
)

class BibliotecaViewModel : ViewModel() {

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

    private val cambiosPendientes = mutableListOf<CambioPendiente>()

    // Variable para controlar la carga
    var guardando by mutableStateOf(false)

    val tieneLibros: Boolean
        get() = biblioteca.librosRecomendados.isNotEmpty() ||
                biblioteca.librosLeidos.isNotEmpty() ||
                biblioteca.librosFuturasLecturas.isNotEmpty()

    // --- CARGAR DATOS ---
    fun cargarBibliotecaReal(usuarioId: Long) {
        viewModelScope.launch(Dispatchers.IO) { // 🎯 IMPORTANTE: Hilo secundario
            try {
                val response = apiService.getBiblioteca(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val nuevoModelo = mapearDTOaModelo(response.body()!!)
                    withContext(Dispatchers.Main) {
                        biblioteca = BibliotecaVacia // Limpiar para refrescar
                        biblioteca = nuevoModelo
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- GESTIÓN VISUAL DE LIBROS ---
    fun agregarLibroAMiBiblioteca(nuevoLibro: Libro, seccion: String) {
        val actual = biblioteca
        biblioteca = when (seccion) {
            "Recomendados" -> actual.copy(librosRecomendados = actual.librosRecomendados + nuevoLibro)
            "Últimos libros" -> actual.copy(librosLeidos = actual.librosLeidos + nuevoLibro)
            "Futuras lecturas" -> actual.copy(librosFuturasLecturas = actual.librosFuturasLecturas + nuevoLibro)
            else -> actual
        }
        // Guardamos el ID del libro para enviarlo luego
        cambiosPendientes.add(CambioPendiente("ADD", seccion, nuevoLibro.id))
    }
    fun agregarLibroConOptimisticUpdate(nuevoLibro: Libro, seccion: String) {
        agregarLibroAMiBiblioteca(nuevoLibro, seccion) // Actualiza UI inmediatamente
    }

    fun eliminarLibro(libro: Libro, seccion: String) {
        val actual = biblioteca
        biblioteca = when (seccion) {
            "Recomendados" -> actual.copy(librosRecomendados = actual.librosRecomendados - libro)
            "Últimos libros" -> actual.copy(librosLeidos = actual.librosLeidos - libro)
            "Futuras lecturas" -> actual.copy(librosFuturasLecturas = actual.librosFuturasLecturas - libro)
            else -> actual
        }
        cambiosPendientes.add(CambioPendiente("DELETE", seccion, libro.id))
    }

    // --- GUARDAR CAMBIOS ---
// Archivo: com.example.pantallas.ui.biblioteca.BibliotecaViewModel.kt

    fun guardarCambiosEnServidor(usuarioId: Long, onTerminado: () -> Unit) {
        if (cambiosPendientes.isEmpty()) {
            onTerminado()
            return
        }

        guardando = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Procesamos todos los cambios pendientes en la DB
                cambiosPendientes.forEach { cambio ->
                    try {
                        when (cambio.tipoAccion) {
                            "ADD" -> when (cambio.seccion) {
                                "Recomendados" -> apiService.agregarLibroARecomendados(usuarioId, cambio.libroId)
                                "Últimos libros" -> apiService.agregarLibroALeidos(usuarioId, cambio.libroId)
                                "Futuras lecturas" -> apiService.agregarLibroAFuturas(usuarioId, cambio.libroId)
                            }
                            "DELETE" -> when (cambio.seccion) {
                                "Recomendados" -> apiService.eliminarLibroDeRecomendados(usuarioId, cambio.libroId)
                                "Últimos libros" -> apiService.eliminarLibroDeLeidos(usuarioId, cambio.libroId)
                                "Futuras lecturas" -> apiService.eliminarLibroDeFuturas(usuarioId, cambio.libroId)
                            }
                        }
                    } catch (e: Exception) { e.printStackTrace() }
                }
                cambiosPendientes.clear()


                val response = apiService.getBiblioteca(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val nuevoModelo = mapearDTOaModelo(response.body()!!)
                    withContext(Dispatchers.Main) {
                        biblioteca = nuevoModelo // Actualizamos el estado observable
                        onTerminado()
                    }
                } else {
                    withContext(Dispatchers.Main) { onTerminado() }
                }
            } finally {
                withContext(Dispatchers.Main) { guardando = false }
            }
        }
    }


    // --- MAPEADORES ---
    private fun mapearDTOaModelo(dto: BibliotecaDTO): Biblioteca {
        return Biblioteca(
            id = 0,
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
            // 🎯 Usamos categoriaNombre que viene del backend
            categoria = Categoria(0, dto.categoriaNombre ?: "General")
        )
    }
}