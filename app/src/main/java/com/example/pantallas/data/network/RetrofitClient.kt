package com.example.pantallas.data.network

import com.example.pantallas.api.LoginApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pantallas.api.LibroApi

object RetrofitClient {
    /**
     * Si pruebas en un dispositivo físico, reemplaza 10.0.2.2 por la
     * IP local de tu PC en tu red, por ejemplo:
     *
     * private const val BASE_URL = "http://192.168.1.50:8080/"
     */
    //si usamos el móvil físico debemos usar la IP local
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia para Login
    val loginApi: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }

    // Instancia para Libros (Agrégala aquí)
    val libroApi: LibroApi by lazy {
        retrofit.create(LibroApi::class.java)
    }
}