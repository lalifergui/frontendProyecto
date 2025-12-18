package com.example.pantallas.ui.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.repository.LibroRepositorio
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibroViewModel : ViewModel() {

    private val repositorio = LibroRepositorio()

    // Lista observable de libros
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> get() = _libros

    init {
        obtenerLibros()
    }

    // Obtener libros del backend
    fun obtenerLibros() {
        viewModelScope.launch {
            try {
                // Ahora getLibros() es suspend, se llama directamente
                val lista = repositorio.getLibros()
                _libros.value = lista
            } catch (e: Exception) {
                println("Error al obtener libros: ${e.message}")
            }
        }
    }

    // Agregar un libro
    fun agregarLibro(titulo: String, autor: String, portada: String, categoria: Categoria) {
        val nuevoLibro = Libro(
            id = 0,
            titulo = titulo,
            autor = autor,
            portada = portada,
            categoria = categoria
        )

        viewModelScope.launch {
            try {
                val libroCreado = repositorio.crearLibro(nuevoLibro)
                _libros.value = _libros.value + libroCreado
            } catch (e: Exception) {
                println("Error al agregar libro: ${e.message}")
            }
        }
    }
}

