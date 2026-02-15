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

class PerfilViewModel : ViewModel() {

    // 🎯 CONFIGURACIÓN: La URL base de tu servidor (usa 10.0.2.2 para el emulador)
    private val BASE_URL_UPLOADS = "http://10.0.2.2:8080/uploads/"

    var perfil: Perfil by mutableStateOf(Perfil(0L, "", "", "", ""))
        private set

    var estaCargando by mutableStateOf(false)
        private set

    fun cargarPerfilReal(usuarioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                estaCargando = true
                perfil = Perfil(0L, "", "", "", "")
            }
            try {
                val response = RetrofitClient.perfilApi.getPerfil(usuarioId)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!

                    // 🎯 PROCESAMIENTO: Si el backend devuelve un nombre de archivo,
                    // le añadimos la dirección del servidor para que Coil lo encuentre.
                    val fotoFinal = if (datos.fotoPerfil.isNullOrEmpty()) {
                        ""
                    } else if (datos.fotoPerfil.startsWith("http")) {
                        datos.fotoPerfil // Si ya es una URL, se deja igual
                    } else {
                        BASE_URL_UPLOADS + datos.fotoPerfil // Si es "foto.jpg", se completa
                    }

                    withContext(Dispatchers.Main) {
                        perfil = Perfil(
                            perfil_id = datos.perfilId ?: 0L,
                            nombre = datos.nombre,
                            apellidos = datos.apellidos,
                            fechaNacimiento = datos.fechaNacimiento,
                            ciudad = datos.ciudad,
                            fotoPerfil = fotoFinal
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) { estaCargando = false }
            }
        }
    }

    fun actualizarEstadoLocal(nuevoPerfil: Perfil) {
        perfil = nuevoPerfil
    }
}