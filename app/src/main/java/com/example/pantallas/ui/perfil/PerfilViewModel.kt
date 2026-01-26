package com.example.pantallas.ui.perfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.launch

class PerfilViewModel : ViewModel() {

    // 1. ESTADO OBSERVABLE: Se inicializa con valores vacíos para evitar datos de ejemplo
    var perfil: Perfil by mutableStateOf(Perfil(0L, "", "", "", ""))
        private set

    var estaCargando by mutableStateOf(false)
        private set

    // 2. CARGA DE DATOS REALES DESDE EL BACKEND
    /**
     * Obtiene los datos reales del perfil desde MySQL a través del backend.
     * Se debe llamar al iniciar la pantalla.
     */
    fun cargarPerfilReal(usuarioId: Long) {
        viewModelScope.launch {
            estaCargando = true
            try {
                val response = RetrofitClient.perfilApi.getPerfil(usuarioId) // <-- Cambiado a perfilApi
                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        perfil = Perfil(
                            perfil_id = datos.perfilId ?: 0L,
                            nombre = datos.nombre,
                            apellidos = datos.apellidos,
                            fechaNacimiento = datos.fechaNacimiento,
                            ciudad = datos.ciudad
                        )
                    }
                }
            } catch (e: Exception) {
                perfil = Perfil.PerfilEjemplo
            } finally {
                estaCargando = false
            }
        }
    }

    // 3. ACTUALIZACIÓN LOCAL (Opcional si ya guardas en EditarPerfil)
    fun actualizarEstadoLocal(nuevoPerfil: Perfil) {
        perfil = nuevoPerfil
    }
}