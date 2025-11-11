package com.example.pantallas.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.pantallas.data.dto.UsuarioDTO
import com.example.pantallas.modelos.Usuario
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RegistroViewModel  : ViewModel(){
    private val _usuario = MutableStateFlow(UsuarioDTO())
    val usuario: StateFlow<UsuarioDTO> = _usuario
    val nombre= MutableStateFlow("");
    private val _errorEmail = MutableStateFlow(false)
    val errorEmail: StateFlow<Boolean> = _errorEmail
    private val _errorPassword = MutableStateFlow(false)
    val errorPassword: StateFlow<Boolean> = _errorPassword

    val botonHabilitado = combine(
        _usuario, _errorPassword,_errorEmail
    ) { f ->
        val dto = f[0] as UsuarioDTO
        val errPassword = f[1] as Boolean
        val errEmail = f[2] as Boolean
        val camposLlenos =
            dto.password.isNotBlank() == true &&
                    dto.email.isNotBlank() == true
        val noErrores = !errPassword && !errEmail

        camposLlenos && noErrores

    }
    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorEmail.value = input.isNotEmpty() && !regexEmail.matches(input)
    }
    private fun validarPassword(input: String) {
        _errorPassword.value = input.isNotEmpty() && input.length < 4
    }

    init {
        // Observa los cambios de los inputs y actualiza los estados de error
        viewModelScope.launch {
            _usuario.collect { dto ->
                validarPassword(input = dto.password)
                validarEmail(input = dto.email)
            }
        }
    }
    fun actualizarUsuario(nuevoUsuario: UsuarioDTO) {
        _usuario.value = nuevoUsuario
    }

}