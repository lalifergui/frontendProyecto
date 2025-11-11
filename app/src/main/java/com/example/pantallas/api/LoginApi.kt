package com.example.pantallas.api


import com.example.pantallas.data.dto.LoginRequest
import com.example.pantallas.data.dto.LoginResponse
import retrofit2.http.Body


import retrofit2.http.POST

interface  LoginApi{
    @POST("auth/login")
    suspend  fun login(@Body request: LoginRequest): LoginResponse
}