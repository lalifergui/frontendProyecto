package com.example.pantallas.api

import com.example.pantallas.data.model.LibroDTO
import com.example.pantallas.modelos.Libro
import com.example.pantallas.ui.libro.LibroCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibroApi {

    @GET("libros")
    suspend fun getLibros(): Response<List<LibroDTO>>

    // CORRECCIÓN: Ahora acepta 'LibroCreateRequest' en lugar de LibroDTO
    @POST("libros")
    suspend fun crearLibro(@Body request: LibroCreateRequest): Response<LibroDTO>
}