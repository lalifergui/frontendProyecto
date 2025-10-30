package com.example.pantallas.api

import com.example.pantallas.modelos.Libro
import retrofit2.Call

class LibroRepositorio {
    private val api = RetrofitClient.instance.create(LibroApi::class.java)

    fun getLibros(): Call<List<Libro>> = api.getLibros()
    fun crearLibros(libro: Libro): Call<Libro> = api.crearLibros(libro)
}