package com.example.pantallas.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.UsuarioRegisterDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    private val _usuario = MutableStateFlow(UsuarioRegisterDTO("", ""))
    val usuario: StateFlow<UsuarioRegisterDTO> = _usuario.asStateFlow()

    private val _usuarioIdGenerado = MutableStateFlow<Long?>(null)
    val usuarioIdGenerado: StateFlow<Long?> = _usuarioIdGenerado.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _errorEmail = MutableStateFlow(false)
    val errorEmail: StateFlow<Boolean> = _errorEmail.asStateFlow()

    private val _errorPassword = MutableStateFlow(false)
    val errorPassword: StateFlow<Boolean> = _errorPassword.asStateFlow()

    //  NUEVO: Estado para el mensaje de error del servidor (Usuario ya existe, etc.)
    private val _mensajeErrorServidor = MutableStateFlow<String?>(null)
    val mensajeErrorServidor: StateFlow<String?> = _mensajeErrorServidor.asStateFlow()

    val botonHabilitado: StateFlow<Boolean> = combine(
        _usuario, _errorEmail, _errorPassword
    ) { user, errE, errP ->
        user.email.isNotBlank() && user.password.isNotBlank() && !errE && !errP
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    //  Corregido: Quitamos 'private' para que Registrar.kt pueda llamarlo
    fun actualizarUsuario(nuevoUsuario: UsuarioRegisterDTO) {
        _usuario.value = nuevoUsuario
        _mensajeErrorServidor.value = null // Limpiar error al escribir
        validarEmail(nuevoUsuario.email)
        validarPassword(nuevoUsuario.password)
    }

    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorEmail.value = input.isNotEmpty() && !regexEmail.matches(input)
    }

    private fun validarPassword(input: String) {
        // Reglas: 8 caracteres, Mayúscula, Minúscula, Número y '*'
        val regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*])(?=\\S+$).{8,}$".toRegex()
        _errorPassword.value = input.isNotEmpty() && !regexPassword.matches(input)
    }

    fun registrar() {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeErrorServidor.value = null
            try {
                val response = RetrofitClient.usuarioApi.registrar(_usuario.value)

                if (response.isSuccessful && response.body() != null) {
                    _usuarioIdGenerado.value = response.body()?.id
                } else {
                    //  Manejo de usuario duplicado (Error 400 o 409)
                    if (response.code() == 400 || response.code() == 409) {
                        _mensajeErrorServidor.value = "Este correo ya está registrado"
                    } else {
                        _mensajeErrorServidor.value = "Error en el registro"
                    }
                    _usuarioIdGenerado.value = null
                }
            } catch (e: Exception) {
                _mensajeErrorServidor.value = "Error de conexión con el servidor"
                _usuarioIdGenerado.value = null
            } finally {
                _estaCargando.value = false
            }
        }
    }
}