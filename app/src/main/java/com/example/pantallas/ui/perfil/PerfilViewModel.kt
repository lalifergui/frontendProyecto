package com.example.pantallas.ui.perfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

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
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                estaCargando = true
                // Opcional: limpiar datos viejos para evitar "parpadeo" de info antigua
                perfil = Perfil(0L, "", "", "", "")
            }
            try {
                val response = RetrofitClient.perfilApi.getPerfil(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    withContext(Dispatchers.Main) {
                        perfil = Perfil(
                            perfil_id = datos.perfilId ?: 0L,
                            nombre = datos.nombre,
                            apellidos = datos.apellidos,
                            fechaNacimiento = datos.fechaNacimiento,
                            ciudad = datos.ciudad,
                            fotoPerfil = datos.fotoPerfil ?: ""
                        )
                    }
                }
            } finally {
                withContext(Dispatchers.Main) { estaCargando = false }
            }
        }
    }

    // 3. ACTUALIZACIÓN LOCAL (Opcional si ya guardas en EditarPerfil)
    fun actualizarEstadoLocal(nuevoPerfil: Perfil) {
        perfil = nuevoPerfil
    }
}