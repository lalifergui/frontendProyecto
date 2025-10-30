package com.example.pantallas.api

import com.example.pantallas.modelos.Libro
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface LibroApi {

    @GET("libros") // La URL que tengas en tu backend
    fun getLibros(): Call<List<Libro>>

    @POST("libros")
    fun crearLibro(@Body libro: Libro): Call<Libro>
}