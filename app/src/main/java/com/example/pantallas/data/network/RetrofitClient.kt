package com.example.pantallas.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pantallas.api.LibroApi
import com.example.pantallas.api.UsuarioApi // Asegúrate de haber renombrado LoginApi
import com.example.pantallas.api.BibliotecaApi
import com.example.pantallas.api.PerfilApi


object RetrofitClient {
    // IMPORTANTE: Si usas móvil real, usa la IP de tu compañera (ej. 192.168.1.50)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Para Login, Registro y Perfil (UsuarioController)
    val usuarioApi: UsuarioApi by lazy {
        retrofit.create(UsuarioApi::class.java)
    }

    // Para el buscador (LibroController)
    val libroApi: LibroApi by lazy {
        retrofit.create(LibroApi::class.java)
    }
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Para guardar libros y ver la biblioteca (BibliotecaController)
    val bibliotecaApi: BibliotecaApi by lazy {
        retrofit.create(BibliotecaApi::class.java)
    }
    // En RetrofitClient.kt
    val perfilApi: PerfilApi by lazy {
        retrofit.create(PerfilApi::class.java)
    }
}