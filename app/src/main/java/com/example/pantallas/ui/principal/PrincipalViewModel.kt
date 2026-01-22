package com.example.pantallas.ui.principal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// Clase auxiliar para agrupar los datos que se muestran en la tarjeta
data class PerfilUsuarioSugerido(
    val perfil: Perfil,
    val biblioteca: Biblioteca
)class PrincipalViewModel : ViewModel() {

    //ESTADO PRINCIPAL: El usuario (con su biblioteca) que estamos explorando
    var usuarioSugerido: PerfilUsuarioSugerido? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(true)
        private set

    var categoriaSeleccionada: Categoria? by mutableStateOf(null)
        private set

    init {
        cargarSiguientePerfil()
    }

    fun filtrarPorCategoria(categoria: Categoria) {
        categoriaSeleccionada = categoria
        cargarSiguientePerfil()
    }

    fun likeUsuario(perfil: Perfil) {
        viewModelScope.launch {
            // Aquí llamarías a tu repositorio para guardar el ID del usuario en favoritos
            println("Añadido ${perfil.nombre} a favoritos")
            cargarSiguientePerfil()
        }
    }

    fun dislikeUsuario() {
        cargarSiguientePerfil()
    }

    private fun cargarSiguientePerfil() {
        viewModelScope.launch {
            isLoading = true
            delay(500)

            val cat = categoriaSeleccionada ?: Categoria(0, "General")

            // SOLUCIÓN: Usamos Perfil.PerfilEjemplo y modificamos solo lo necesario
            usuarioSugerido = PerfilUsuarioSugerido(
                perfil = Perfil.PerfilEjemplo.copy(
                    perfil_id = (1..1000).random().toLong(),
                    nombre = "Usuario ${(10..99).random()}"
                    // No ponemos bio porque no existe en tu modelo.
                    // apellidos, ciudad, etc., se mantienen los del ejemplo.
                ),
                biblioteca = Biblioteca(
                    id = (1..1000).random().toLong(),
                    usuario = null,
                    librosRecomendados = listOf(
                        Libro(1, "Donde los Árboles Cantan", "Laura Gallego", "", cat)
                    ),
                    librosLeidos = listOf(
                        Libro(2, "Reina Roja", "Juan Gómez-Jurado", "", cat)
                    ),
                    librosFuturasLecturas = emptyList()
                )
            )
            isLoading = false
        }
    }
}