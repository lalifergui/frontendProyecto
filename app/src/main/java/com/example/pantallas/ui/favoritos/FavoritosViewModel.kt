package com.example.pantallas.ui.favoritos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.NotificacionesFavoritos
import com.example.pantallas.modelos.UsuariosFavoritos
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritosViewModel : ViewModel() {
    private val api = RetrofitClient.usuarioApi

    var pestañaActual by mutableStateOf("usuarios")
    var listaFavoritos by mutableStateOf<List<UsuariosFavoritos>>(emptyList())
    var isLoading by mutableStateOf(false)

    var listaNotificaciones by mutableStateOf<List<NotificacionesFavoritos>>(emptyList())
    // Dentro de FavoritosViewModel.kt
    fun cargarFavoritos(usuarioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val response = api.getFavoritos(usuarioId) // Llama a la API

                if (response.isSuccessful && response.body() != null) {
                    // Mapeamos los datos que vienen del servidor
                    val favoritosMapeados = response.body()!!.map { dto ->
                        UsuariosFavoritos(
                            id = dto.usuarioId,
                            nombre = dto.nombre,
                            perfil = Perfil(
                                perfil_id = dto.usuarioId,
                                nombre = dto.nombre,
                                apellidos = "",
                                ciudad = dto.ciudad,
                                fechaNacimiento = "2000-01-01",
                                fotoPerfil = dto.fotoPerfil ?: ""
                            ),
                            ultimaActualizacion = "Hace un momento"
                        )
                    }

                    withContext(Dispatchers.Main) {
                        // Actualizamos la variable de estado de la UI
                        listaFavoritos = favoritosMapeados
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false // Cerramos el cargando siempre
                }
            }
        }
    }

    fun cambiarPestaña(nuevaPestaña: String) {
        pestañaActual = nuevaPestaña
    }
    // En FavoritosViewModel.kt
    fun eliminarFavorito(miId: Long, favoritoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.eliminarFavorito(miId, favoritoId) //

                if (response.isSuccessful) {
                    println("DEBUG: Borrado exitoso, recargando lista...")
                    // 🎯 CLAVE: Volvemos a cargar los favoritos para actualizar la lista en la UI
                    cargarFavoritos(miId) //
                    println("DEBUG: Usuario $favoritoId eliminado de favoritos de $miId")
                } else {
                    println("DEBUG: Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}