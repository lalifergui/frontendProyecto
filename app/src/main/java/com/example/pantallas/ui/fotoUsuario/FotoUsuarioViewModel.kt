package com.example.pantallas.ui.fotoUsuario

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * , el perfil nace vacío. Debemos asegurarnos de que la actualización sea
 * "atómica" para que la UI no vea el estado vacío ni un segundo.
 */
class FotoUsuarioViewModel : ViewModel() {
    private val _perfil = MutableStateFlow(Perfil(0L, "", "", "", ""))
    val perfil: StateFlow<Perfil> = _perfil.asStateFlow()

    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri.asStateFlow()

    // 🎯 Sincronización inmediata
    fun setDatosPerfil(nombre: String, apellidos: String, ciudad: String, fecha: String) {
        _perfil.value = Perfil(
            perfil_id = 0L, // ID temporal para la vista
            nombre = nombre,
            apellidos = apellidos,
            ciudad = ciudad,
            fechaNacimiento = fecha,
            fotoPerfil = ""
        )
    }

    fun actualizarFoto(uri: Uri) {
        _fotoUri.value = uri
    }
}