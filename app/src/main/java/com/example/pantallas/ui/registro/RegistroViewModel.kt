package com.example.pantallas.ui.registro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegistroViewModel  : ViewModel(){
    val nombre= MutableStateFlow("");
    val apellidos = MutableStateFlow("");
    val fechaNacimiento= MutableStateFlow("");
    val ciudad =MutableStateFlow("");
    val email =MutableStateFlow("");
    val password =MutableStateFlow("");

    fun actualizarNombre(nuevoNombre: String) {
        nombre.value = nuevoNombre
    }

    fun actualizarApellidos(nuevoApellido: String) {
        apellidos.value = nuevoApellido
    }

    fun actualizarFechaNacimiento(nuevoFechaNacimiento: String) {
        fechaNacimiento.value = nuevoFechaNacimiento
    }

    fun actualizarCiudad(nuevoCiudad: String) {
        ciudad.value = nuevoCiudad
    }

    fun actualizarEmail(nuevoEmail: String) {
        email.value = nuevoEmail
    }
    fun actualizarPassword(nuevoPassword: String) {
        password.value = nuevoPassword
    }
}