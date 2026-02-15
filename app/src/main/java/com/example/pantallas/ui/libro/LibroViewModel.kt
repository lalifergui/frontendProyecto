package com.example.pantallas.ui.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// CORRECCIÓN: Solo un import de RetrofitClient (Asegúrate que la ruta es correcta)
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Clase auxiliar para enviar al backend
data class LibroCreateRequest(
    val titulo: String,
    val autor: String,
    val portada: String? = "",
    val categoriaId: Long
)

class LibroViewModel : ViewModel() {

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> get() = _libros

    init {
        obtenerLibros()
    }

    fun obtenerLibros() {
        viewModelScope.launch {
            try {
                // Asegúrate que 'libroApi' existe en tu RetrofitClient
                val response = RetrofitClient.libroApi.getLibros()

                if (response.isSuccessful && response.body() != null) {
                    val listaDTO = response.body()!!
                    _libros.value = listaDTO.map { dto ->
                        Libro(
                            id = dto.id ?: 0L,
                            titulo = dto.titulo,
                            autor = dto.autor,
                            portada = dto.portada ?: "",
                            // CORRECCIÓN: Usamos 0L (Long) en lugar de 0 (Int)
                            categoria = Categoria(id = 0L, nombre = dto.categoriaNombre ?: "General")
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun crearLibroManualmente(
        titulo: String,
        autor: String,
        categoria: Categoria,
        onResult: (Libro?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = LibroCreateRequest(
                    titulo = titulo,
                    autor = autor,
                    portada = "",
                    categoriaId = categoria.id
                )
                val response = RetrofitClient.libroApi.crearLibro(request)

                // 1. Usamos una variable segura para el cuerpo
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    // 2. Creamos el libro usando el ID o un valor por
                    // defecto (0L) si fallara
                    val libroReal = Libro(
                        id = body.id ?: 0L,
                        titulo = body.titulo,
                        autor = body.autor,
                        portada = body.portada ?: "",
                        categoria = categoria
                    )
                    _libros.value = _libros.value + libroReal
                    onResult(libroReal)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}