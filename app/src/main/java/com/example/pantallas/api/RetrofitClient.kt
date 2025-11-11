package com.example.pantallas.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    /**
     * Si pruebas en un dispositivo físico, reemplaza 10.0.2.2 por la
     * IP local de tu PC en tu red, por ejemplo:
     *
     * private const val BASE_URL = "http://192.168.1.50:8080/"
     */
    //si usamos el móvil físico debemos usar la IP local
    private const val BASE_URL = "http://10.0.2.2:8080/" // cambia al endpoint real

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //agregamos loginApi para no tener que crear la instancia
    val loginApi: LoginApi by lazy {
        instance.create(LoginApi::class.java)
    }
}