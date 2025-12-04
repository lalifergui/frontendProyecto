package com.example.pantallas.data.repository

import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.data.model.LoginRequest
import com.example.pantallas.data.model.LoginResponse

class LoginRepositorio {
    private  val api= RetrofitClient.loginApi

    suspend fun login(usuario:String, password:String): LoginResponse{
        val request = LoginRequest(usuario,password)
        return  api.login(request)
    }

}