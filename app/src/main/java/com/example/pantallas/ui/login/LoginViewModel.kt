package com.example.pantallas.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.LoginRequestDTO
import com.example.pantallas.data.model.UsuarioDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _errorEmail = MutableStateFlow(false)
    val errorEmail = _errorEmail.asStateFlow()

    private val _errorPassword = MutableStateFlow(false)
    val errorPassword = _errorPassword.asStateFlow()

    // Nuevo estado para errores de credenciales (400, 401, 404)
    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    val botonHabilitado: StateFlow<Boolean> = combine(
        _email, _password, _errorEmail, _errorPassword
    ) { email, pass, errE, errP ->
        email.isNotBlank() && pass.isNotBlank() && !errE && !errP
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _loginResult = MutableStateFlow<UsuarioDTO?>(null)
    val loginResult = _loginResult.asStateFlow()

    fun onEmailChanged(nuevoEmail: String) {
        _email.value = nuevoEmail
        _errorLogin.value = null // Limpia error al escribir
        validarEmail(nuevoEmail)
    }

    fun onPasswordChanged(nuevaPass: String) {
        _password.value = nuevaPass
        _errorLogin.value = null // Limpia error al escribir
        validarPassword(nuevaPass)
    }

    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorEmail.value = input.isNotEmpty() && !regexEmail.matches(input)
    }

    private fun validarPassword(input: String) {
        val regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*])(?=\\S+$).{8,}$".toRegex()
        _errorPassword.value = input.isNotEmpty() && !regexPassword.matches(input)
    }

    fun login() {
        viewModelScope.launch {
            try {
                _errorLogin.value = null
                val response = RetrofitClient.usuarioApi.login(LoginRequestDTO(_email.value, _password.value))

                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                } else {
                    //  CONTROL DE ERROR DE CREDENCIALES
                    _errorLogin.value = "Email o contraseña incorrectos"
                    _loginResult.value = null
                }
            } catch (e: Exception) {
                _errorLogin.value = "Error de conexión: Verifica tu internet"
                _loginResult.value = null
            }
        }
    }
}