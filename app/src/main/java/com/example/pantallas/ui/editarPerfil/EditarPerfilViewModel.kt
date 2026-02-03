package com.example.pantallas.ui.editarPerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.PerfilUpdateDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Estado para el error visual de la fecha
    private val _errorFecha = MutableStateFlow(false)
    val errorFecha: StateFlow<Boolean> = _errorFecha.asStateFlow()

    // Lógica del botón: habilitado solo si hay datos y la fecha es correcta
    val botonHabilitado: Boolean
        get() = nombre.isNotBlank() &&
                apellidos.isNotBlank() &&
                fechaNacimiento.isNotBlank() &&
                ciudad.isNotBlank() &&
                !_errorFecha.value //  No permite guardar si hay error de formato
// Dentro de EditarPerfilViewModel.kt

    private val _errorNombre = MutableStateFlow(false)
    val errorNombre = _errorNombre.asStateFlow()
    val regexNombre = "^[A-ZÁÉÍÓÚÑa-záéíóúñü\\s]*$".toRegex()

    fun onNombreChanged(nuevoNombre: String) {
        nombre = nuevoNombre

        _errorNombre.value = nuevoNombre.isNotEmpty() && !regexNombre.matches(nuevoNombre)
    }

    // Maneja el cambio de fecha y dispara la validación
    fun onFechaNacimientoChanged(nuevaFecha: String) {
        fechaNacimiento = nuevaFecha
        validarFormatoFecha(nuevaFecha)
    }

    // Validación mediante Regex (AAAA-MM-DD)
    private fun validarFormatoFecha(input: String) {
        // Explicación: 4 dígitos - mes 01-12 - día 01-31
        val regexFecha = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$".toRegex()
        _errorFecha.value = input.isNotEmpty() && !regexFecha.matches(input)
    }

    // Cargar datos actuales desde el servidor
    fun cargarDatosParaEditar(usuarioId: Long) {
        viewModelScope.launch {
            estaCargando = true
            try {
                val response = RetrofitClient.perfilApi.getPerfil(usuarioId)
                if (response.isSuccessful) {
                    response.body()?.let { p ->
                        nombre = p.nombre
                        apellidos = p.apellidos
                        ciudad = p.ciudad
                        fechaNacimiento = p.fechaNacimiento
                        // Validamos la fecha cargada por si viniera en formato incorrecto
                        validarFormatoFecha(p.fechaNacimiento)
                    }
                }
            } catch (e: Exception) {
                mensajeError = "Error al cargar datos"
            } finally {
                estaCargando = false
            }
        }
    }

    fun limpiarCampos() {
        nombre = ""
        apellidos = ""
        ciudad = ""
        fechaNacimiento = ""
        _errorFecha.value = false
    }

    // Función para guardar los cambios en MySQL
    fun actualizarPerfil(usuarioId: Long, onSuccess: () -> Unit) {
        if (!botonHabilitado) return

        val dto = PerfilUpdateDTO(
            nombre = nombre,
            apellidos = apellidos,
            fechaNacimiento = fechaNacimiento,
            ciudad = ciudad,
            fotoPerfil = fotoPerfil
        )

        viewModelScope.launch {
            try {
                estaCargando = true
                mensajeError = null

                val response = RetrofitClient.perfilApi.actualizarPerfil(usuarioId, dto)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    // Control de error 500 o 400 del servidor
                    mensajeError = "Servidor: ${response.code()} - Formato de fecha no aceptado"
                }
            } catch (e: Exception) {
                mensajeError = "Error de red: ${e.message}"
            } finally {
                estaCargando = false
            }
        }
    }
}