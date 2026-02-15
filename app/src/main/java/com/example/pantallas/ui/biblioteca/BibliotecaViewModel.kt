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

    var guardando by mutableStateOf(false)

    val tieneLibros: Boolean
        get() = biblioteca.librosRecomendados.isNotEmpty() ||
                biblioteca.librosLeidos.isNotEmpty() ||
                biblioteca.librosFuturasLecturas.isNotEmpty()

    // --- CARGAR DATOS ---
    fun cargarBibliotecaReal(usuarioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                biblioteca = BibliotecaVacia
            }
            try {
                val response = apiService.getBiblioteca(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val nuevoModelo = mapearDTOaModelo(response.body()!!)
                    withContext(Dispatchers.Main) {
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
        cambiosPendientes.add(CambioPendiente("ADD", seccion, nuevoLibro.id))
    }

    // CORRECCIÓN: Función de reemplazo completa
    fun reemplazarLibro(libroViejo: Libro, libroNuevo: Libro, seccion: String) {
        val actual = biblioteca

        // 1. Actualizamos la UI reemplazando el libro en la lista correspondiente
        biblioteca = when (seccion) {
            "Recomendados" -> actual.copy(
                librosRecomendados = actual.librosRecomendados.map { if (it.id == libroViejo.id) libroNuevo else it }
            )
            "Últimos libros" -> actual.copy(
                librosLeidos = actual.librosLeidos.map { if (it.id == libroViejo.id) libroNuevo else it }
            )
            "Futuras lecturas" -> actual.copy(
                librosFuturasLecturas = actual.librosFuturasLecturas.map { if (it.id == libroViejo.id) libroNuevo else it }
            )
            else -> actual
        }

        // 2. Registramos la intención en el backend: quitar el viejo y poner el nuevo
        cambiosPendientes.add(CambioPendiente("DELETE", seccion, libroViejo.id))
        cambiosPendientes.add(CambioPendiente("ADD", seccion, libroNuevo.id))
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
    fun guardarCambiosEnServidor(usuarioId: Long, onTerminado: () -> Unit) {
        if (cambiosPendientes.isEmpty()) {
            onTerminado()
            return
        }

        guardando = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
                        biblioteca = nuevoModelo
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
            portada = if (dto.portada.isNullOrEmpty()) "default_book_cover" else dto.portada,
            categoria = Categoria(0, dto.categoriaNombre ?: "General")
        )
    }
}