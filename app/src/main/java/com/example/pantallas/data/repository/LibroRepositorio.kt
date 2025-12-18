package com.example.pantallas.data.repository

import com.example.pantallas.api.LibroApi
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Libro
import retrofit2.Call

class LibroRepositorio {

    private val api = RetrofitClient.libroApi

    suspend fun getLibros() = api.getLibros()
    suspend fun crearLibro(libro: Libro) = api.crearLibro(libro)
}