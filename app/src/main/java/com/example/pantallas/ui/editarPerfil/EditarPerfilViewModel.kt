package com.example.pantallas.ui.editarPerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.launch

class EditarPerfilViewModel : ViewModel() {

    // Estados para los campos de la pantalla
    var nombre by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var ciudad by mutableStateOf("")
    var fotoPerfil by mutableStateOf("")

    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    // Función para cargar los datos actuales del perfil
    fun cargarPerfil(perfilId: Long) {
        viewModelScope.launch {
            estaCargando = true
            try {
                // Asumiendo que crearás PerfilApi en RetrofitClient
                // val response = RetrofitClient.perfilApi.obtenerPerfil(perfilId)
                // nombre = response.nombre
                // apellidos = response.apellidos...
            } catch (e: Exception) {
                mensajeError = "Error al cargar datos"
            } finally {
                estaCargando = false
            }
        }
    }

    // Función para guardar los cambios
    fun actualizarPerfil(usuarioId: Long) {
        val perfilEditado = PerfilDTO(
            nombre = nombre,
            apellidos = apellidos,
            fechaNacimiento = fechaNacimiento,
            ciudad = ciudad,
            fotoPerfil = fotoPerfil,
            usuarioId = usuarioId
        )

        viewModelScope.launch {
            try {
                // Aquí llamarías a tu repositorio o API directamente
                // RetrofitClient.perfilApi.actualizarPerfil(perfilEditado)
                println("Perfil enviado: $perfilEditado")
            } catch (e: Exception) {
                mensajeError = "No se pudo actualizar el perfil"
            }
        }
    }
}