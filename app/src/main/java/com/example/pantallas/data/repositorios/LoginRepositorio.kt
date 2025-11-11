package com.example.pantallas.data.repositorios

import com.example.pantallas.api.RetrofitClient
import com.example.pantallas.data.dto.LoginRequest
import com.example.pantallas.data.dto.LoginResponse

class LoginRepositorio {
    private  val api= RetrofitClient.loginApi

    suspend fun login(usuario:String, password:String): LoginResponse{
        val request = LoginRequest(usuario,password)
        return  api.login(request)
    }

}