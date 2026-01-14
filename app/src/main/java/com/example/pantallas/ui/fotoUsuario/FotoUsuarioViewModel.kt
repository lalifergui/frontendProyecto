package com.example.pantallas.ui.fotoUsuario

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FotoUsuarioViewModel : ViewModel() {
    // Simulación de datos del perfil recién creado
    private val _perfil = MutableStateFlow(Perfil.PerfilEjemplo)
    val perfil: StateFlow<Perfil> = _perfil

    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri

    fun actualizarFoto(uri: Uri) {
        _fotoUri.value = uri
    }
}