package com.example.pantallas.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pantallas.api.LibroApi
import com.example.pantallas.api.UsuarioApi // Asegúrate de haber renombrado LoginApi
import com.example.pantallas.api.BibliotecaApi
import com.example.pantallas.api.PerfilApi
import okhttp3.OkHttpClient


object RetrofitClient {
    // Cambia esto
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            var res = chain.proceed(chain.request())
            var tryCount = 0
            while (!res.isSuccessful && tryCount < 3) {
                tryCount++
                res = chain.proceed(chain.request())
            }
            res
        }.build()

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