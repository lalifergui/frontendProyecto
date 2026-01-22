package com.example.pantallas.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.UsuarioDTO
import com.example.pantallas.data.model.UsuarioRegisterDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    // Usamos UsuarioRegisterDTO para incluir el password
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

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    val botonHabilitado: StateFlow<Boolean> = combine(
        _usuario, _errorEmail, _errorPassword
    ) { user, errE, errP ->
        user.email.isNotBlank() && user.password.length >= 4 && !errE && !errP
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun actualizarUsuario(nuevoUsuario: UsuarioRegisterDTO) {
        _usuario.value = nuevoUsuario
        validarEmail(nuevoUsuario.email)
        validarPassword(nuevoUsuario.password)
    }

    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorEmail.value = input.isNotEmpty() && !regexEmail.matches(input)
    }

    private fun validarPassword(input: String) {
        _errorPassword.value = input.isNotEmpty() && input.length < 4
    }

    fun registrar() {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                val response = RetrofitClient.usuarioApi.registrar(_usuario.value)

                if (response.isSuccessful && response.body() != null) {
                    //  Guardamos el ID real que viene de MySQL
                    _usuarioIdGenerado.value = response.body()?.id
                } else {
                    _usuarioIdGenerado.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _usuarioIdGenerado.value = null
            } finally {
                _estaCargando.value = false
            }
        }
    }
}