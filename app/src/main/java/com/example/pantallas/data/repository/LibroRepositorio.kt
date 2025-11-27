package com.example.pantallas.data.repository

import com.example.pantallas.api.LibroApi
import com.example.pantallas.api.RetrofitClient
import com.example.pantallas.modelos.Libro
import retrofit2.Call

class LibroRepositorio {
    private val api = RetrofitClient.instance.create(LibroApi::class.java)

    fun getLibros(): Call<List<Libro>> = api.getLibros()
    fun crearLibro(libro: Libro): Call<Libro> = api.crearLibro(libro)
}