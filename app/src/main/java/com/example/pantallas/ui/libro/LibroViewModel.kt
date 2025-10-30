package com.example.pantallas.ui.libro

import androidx.lifecycle.ViewModel
import com.example.pantallas.api.LibroRepositorio
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        repositorio.getLibros().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if (response.isSuccessful) {
                    _libros.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("Error al obtener libros: ${t.message}")
            }
        })
    }

    // Agregar un libro
    fun agregarLibro(titulo: String, autor: String, portada: String, categoria: Categoria) {
        val libro = Libro(
            id = 0, // si el backend genera el id automáticamente
            titulo = titulo,
            autor = autor,
            portada = portada,
            categoria = categoria
        )

        repositorio.crearLibro(libro).enqueue(object : Callback<Libro> {
            override fun onResponse(call: Call<Libro>, response: Response<Libro>) {
                if (response.isSuccessful) {
                    _libros.value = _libros.value + (response.body() ?: libro)
                }
            }

            override fun onFailure(call: Call<Libro>, t: Throwable) {
                println("Error al agregar libro: ${t.message}")
            }
        })
    }
}
