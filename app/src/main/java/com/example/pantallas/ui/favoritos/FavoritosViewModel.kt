package com.example.pantallas.ui.favoritos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.NotificacionesFavoritos
import com.example.pantallas.modelos.UsuariosFavoritos
import com.example.pantallas.modelos.Perfil

class FavoritosViewModel : ViewModel() {

    // Estado de la pestaña: empieza en "inicio" para pantalla en blanco
    var pestañaActual by mutableStateOf("inicio")
        private set


    var listaFavoritos by mutableStateOf(listOf(
        UsuariosFavoritos(
            id = 1L,
            nombre = "Carlos",
            perfil = Perfil.PerfilEjemplo,
            ultimaActualizacion = "Hace 5 min"
        )
    ))

    var listaNotificaciones by mutableStateOf(listOf(
        NotificacionesFavoritos(1, "Carlos", "ha modificado su biblioteca", "10:30 AM")
    ))

    fun cambiarPestaña(nuevaPestaña: String) {
        pestañaActual = nuevaPestaña
    }
}