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

                // CORRECCIÓN: Esto funcionará ahora que actualizaste LibroApi
                val response = RetrofitClient.libroApi.crearLibro(request)

                if (response.isSuccessful && response.body() != null) {
                    val libroCreadoDTO = response.body()!!

                    val libroReal = Libro(
                        id = libroCreadoDTO.id!!,
                        titulo = libroCreadoDTO.titulo,
                        autor = libroCreadoDTO.autor,
                        portada = libroCreadoDTO.portada ?: "",
                        categoria = categoria
                    )

                    _libros.value = _libros.value + libroReal
                    onResult(libroReal)
                } else {
                    println("Error backend: ${response.code()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}