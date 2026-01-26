package com.example.pantallas.data.repository

import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Libro

class LibroRepositorio {

    private val api = RetrofitClient.libroApi

    suspend fun getLibros() = api.getLibros()
    //uspend fun crearLibro(libro: Libro) = api.crearLibro(libro)
}