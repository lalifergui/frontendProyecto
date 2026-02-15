package com.example.pantallas.ui.editarPerfil

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.PerfilUpdateDTO
import com.example.pantallas.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditarPerfilViewModel : ViewModel() {

    // 1. ESTADOS DE LOS CAMPOS
    var nombre by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var ciudad by mutableStateOf("")
    var fotoPerfil by mutableStateOf("") // Aquí guardaremos el nombre que devuelva el servidor

    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    // 2. VALIDACIONES VISUALES
    private val _errorFecha = MutableStateFlow(false)
    val errorFecha: StateFlow<Boolean> = _errorFecha.asStateFlow()

    private val _errorNombre = MutableStateFlow(false)
    val errorNombre = _errorNombre.asStateFlow()
    val regexNombre = "^[A-ZÁÉÍÓÚÑa-záéíóúñü\\s]*$".toRegex()

    // Lógica del botón: solo habilitado si los datos son válidos
    val botonHabilitado: Boolean
        get() = nombre.isNotBlank() &&
                apellidos.isNotBlank() &&
                fechaNacimiento.isNotBlank() &&
                ciudad.isNotBlank() &&
                !_errorFecha.value &&
                !_errorNombre.value

    // --- MANEJO DE CAMBIOS ---

    fun onNombreChanged(nuevoNombre: String) {
        nombre = nuevoNombre
        _errorNombre.value = nuevoNombre.isNotEmpty() && !regexNombre.matches(nuevoNombre)
    }

    fun onFechaNacimientoChanged(nuevaFecha: String) {
        fechaNacimiento = nuevaFecha
        validarFormatoFecha(nuevaFecha)
    }

    private fun validarFormatoFecha(input: String) {
        // Formato AAAA-MM-DD exigido por MySQL
        val regexFecha = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$".toRegex()
        _errorFecha.value = input.isNotEmpty() && !regexFecha.matches(input)
    }

    // --- OPERACIONES DE RED ---

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
                        fotoPerfil = p.fotoPerfil ?: ""
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

    fun subirImagen(context: Context, uri: Uri, usuarioId: Long) {
        // 🎯 Forzamos que todo el proceso ocurra en un hilo de fondo (IO)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    estaCargando = true
                    mensajeError = null
                }

                // 1. Abrir el archivo de forma segura
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.use { it.readBytes() } // .use cierra el stream automáticamente

                if (bytes == null) {
                    withContext(Dispatchers.Main) { mensajeError = "No se pudo leer la imagen" }
                    return@launch
                }

                // 2. Preparar el Multipart
                val requestFile = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", "perfil_$usuarioId.jpg", requestFile)

                // 3. Llamada a la API
                val response = RetrofitClient.bibliotecaApi.subirFoto(usuarioId, body)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        fotoPerfil = response.body() ?: ""
                        mensajeError = "¡Imagen actualizada!"
                    } else {
                        // 🎯 Si entra aquí, el error es del Servidor (Sandra)
                        mensajeError = "Error servidor: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // 🎯 Si entra aquí, el error es de Red (IP incorrecta o Firewall)
                    mensajeError = "Error de conexión: Verifica la IP"
                    e.printStackTrace()
                }
            } finally {
                withContext(Dispatchers.Main) { estaCargando = false }
            }
        }
    }
    /**
     * GUARDAR PERFIL COMPLETO
     * Actualiza los datos de texto en MySQL.
     */
    fun actualizarPerfil(usuarioId: Long, onSuccess: () -> Unit) {
        if (!botonHabilitado) return

        val dto = PerfilUpdateDTO(
            nombre = nombre,
            apellidos = apellidos,
            fechaNacimiento = fechaNacimiento,
            ciudad = ciudad,
            fotoPerfil = fotoPerfil // Enviamos el nombre de la foto ya subida
        )

        viewModelScope.launch {
            try {
                estaCargando = true
                mensajeError = null

                val response = RetrofitClient.perfilApi.actualizarPerfil(usuarioId, dto)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    mensajeError = "Error ${response.code()}: No se pudo guardar el perfil"
                }
            } catch (e: Exception) {
                mensajeError = "Error de red: ${e.message}"
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
        fotoPerfil = ""
        _errorFecha.value = false
        _errorNombre.value = false
    }
}