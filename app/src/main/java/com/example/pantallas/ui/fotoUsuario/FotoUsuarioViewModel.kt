package com.example.pantallas.ui.fotoUsuario

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FotoUsuarioViewModel : ViewModel() {
    // Inicializamos con un perfil vacío en lugar del de ejemplo
    // 🎯 Hemos añadido un "" extra al final para la ciudad que faltaba
    private val _perfil = MutableStateFlow(Perfil(0L, "", "", "", ""))
    val perfil: StateFlow<Perfil> = _perfil

    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri

    // Función para actualizar los datos con lo que viene del Intent
    fun setDatosPerfil(nombre: String, apellidos: String, ciudad: String, fecha: String) {
        _perfil.value = Perfil(
            nombre = nombre,
            apellidos = apellidos,
            ciudad = ciudad,
            fechaNacimiento = fecha
        )
    }

    fun actualizarFoto(uri: Uri) {
        _fotoUri.value = uri
    }
}