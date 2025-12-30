package com.example.pantallas.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.pantallas.data.model.UsuarioDTO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
class RegistroViewModel : ViewModel() {
    private val _usuario = MutableStateFlow(UsuarioDTO())
    val usuario: StateFlow<UsuarioDTO> = _usuario

    private val _errorEmail = MutableStateFlow(false)
    val errorEmail: StateFlow<Boolean> = _errorEmail

    private val _errorPassword = MutableStateFlow(false)
    val errorPassword: StateFlow<Boolean> = _errorPassword

    // Lógica para habilitar el botón solo con todos los campos llenos y sin errores
    val botonHabilitado = combine(
        _usuario, _errorPassword, _errorEmail
    ) { dto, errPassword, errEmail ->
        // Comprobamos que ABSOLUTAMENTE todos los campos tengan texto
        val camposLlenos = dto.usuario.isNotBlank() &&
                dto.apellidos.isNotBlank() &&
                dto.fechaNacimiento.isNotBlank() &&
                dto.ciudad.isNotBlank() &&
                dto.email.isNotBlank() &&
                dto.password.isNotBlank()

        val noErrores = !errPassword && !errEmail

        camposLlenos && noErrores
    }

    fun actualizarUsuario(nuevoUsuario: UsuarioDTO) {
        _usuario.value = nuevoUsuario
    }

    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorEmail.value = input.isNotEmpty() && !regexEmail.matches(input)
    }

    private fun validarPassword(input: String) {
        _errorPassword.value = input.isNotEmpty() && input.length < 4
    }

    init {
        viewModelScope.launch {
            _usuario.collect { dto ->
                validarPassword(dto.password)
                validarEmail(dto.email)
            }
        }
    }
}