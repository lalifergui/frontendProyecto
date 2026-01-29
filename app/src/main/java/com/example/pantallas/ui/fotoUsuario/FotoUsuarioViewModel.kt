package com.example.pantallas.ui.fotoUsuario

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel encargado de la previsualización de la carta de presentación.
 * El perfil nace con valores por defecto y se actualiza con los datos recibidos del registro.
 */
class FotoUsuarioViewModel : ViewModel() {

    // Estado del perfil para la CardPerfil
    private val _perfil = MutableStateFlow(Perfil(0L, "", "", "", ""))
    val perfil: StateFlow<Perfil> = _perfil.asStateFlow()

    // Estado de la URI de la foto seleccionada
    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri.asStateFlow()

    /**
     * Setea los datos iniciales que vienen del Intent de la pantalla de datos personales.
     */
    fun setDatosPerfil(nombre: String, apellidos: String, ciudad: String, fecha: String) {
        _perfil.value = Perfil(
            perfil_id = 0L,
            nombre = nombre,
            apellidos = apellidos,
            ciudad = ciudad,
            fechaNacimiento = fecha,
            fotoPerfil = ""
        )
    }

    /**
     * Actualiza la URI de la foto para que la previsualización la muestre.
     */
    fun actualizarFoto(uri: Uri) {
        _fotoUri.value = uri
    }
}