package com.example.pantallas.api


import com.example.pantallas.data.model.LoginRequestDTO
import com.example.pantallas.data.model.LoginResponse
import retrofit2.http.Body


import retrofit2.http.POST

interface  LoginApi{
    @POST("auth/login")
    suspend  fun login(@Body request: LoginRequestDTO): LoginResponse
}