package com.example.pantallas.ui.editarPerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.model.PerfilUpdateDTO
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
    val botonHabilitado: Boolean
        get() = nombre.isNotBlank() &&
                apellidos.isNotBlank() &&
                fechaNacimiento.isNotBlank() &&
                ciudad.isNotBlank()


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
    fun cargarDatosParaEditar(usuarioId: Long) {
        viewModelScope.launch {
            estaCargando = true
            try {
                // Usamos perfilApi que ya devuelve Response<PerfilDTO>
                val response = RetrofitClient.perfilApi.getPerfil(usuarioId)

                if (response.isSuccessful) {
                    response.body()?.let { p ->
                        nombre = p.nombre
                        apellidos = p.apellidos
                        ciudad = p.ciudad
                        fechaNacimiento = p.fechaNacimiento
                    }
                }
            } catch (e: Exception) {
                mensajeError = "Error al cargar datos"
            } finally {
                estaCargando = false
            }
        }
    }
    fun limpiarCampos(){
        nombre=""
        apellidos=""
        ciudad=""
        fechaNacimiento=""
    }


    // Función para guardar los cambios

    fun actualizarPerfil(usuarioId: Long, onSuccess: () -> Unit) {
        val dto = PerfilUpdateDTO(
            nombre = nombre,
            apellidos = apellidos,
            fechaNacimiento = fechaNacimiento, // Asegúrate que el formato sea YYYY-MM-DD para Java LocalDate
            ciudad = ciudad,
            fotoPerfil = fotoPerfil
        )

        viewModelScope.launch {
            try {
                estaCargando = true
                val response = RetrofitClient.perfilApi.actualizarPerfil(usuarioId, dto)
                if (response.isSuccessful) {
                    onSuccess() // Si el servidor responde OK, ejecutamos la navegación
                } else {
                    mensajeError = "Error del servidor al guardar"
                }
            } catch (e: Exception) {
                mensajeError = "Error de red: ${e.message}"
            } finally {
                estaCargando = false
            }
        }
    }
}