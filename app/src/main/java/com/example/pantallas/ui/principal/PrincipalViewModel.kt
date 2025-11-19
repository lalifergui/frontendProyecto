package com.example.pantallas.ui.principal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.modelos.Libro
import kotlinx.coroutines.launch

// Asumimos que tienes una forma de identificar qué biblioteca se está viendo (e.g., un ID de usuario/biblioteca)

class PrincipalViewModel : ViewModel() {

    // --- ESTADO DE LA VISTA ---
    var libroActual: Libro? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(true)
        private set

    // ID o índice que indica qué biblioteca/usuario estamos viendo actualmente
    var bibliotecaActualId: Long by mutableStateOf(1L)
        private set

    init {
        // Cargar el primer libro al iniciar
        cargarSiguienteLibro()
    }

    // --- LÓGICA DE SWIPE ---

    /**
     * Registra el "Me Gusta" (Like), guarda el usuario y carga el siguiente libro.
     * @param libro: El libro que recibió el like.
     * @param usuarioTargetId: El ID del usuario cuya biblioteca se está viendo.
     */
    fun likeLibro(libro: Libro, usuarioTargetId: Long) {
        viewModelScope.launch {
            // 1. **GUARDAR USUARIO (Lógica de negocio)**
            //    Aquí llamarías a tu Repositorio para guardar el 'usuarioTargetId'
            //    en la lista de usuarios favoritos del usuario que está haciendo el swipe.
            println("Guardando usuario $usuarioTargetId en favoritos.")

            // 2. **Transicionar la Biblioteca**
            transicionarASiguienteBiblioteca()

            // 3. Cargar el siguiente libro
            cargarSiguienteLibro()
        }
    }

    /**
     * Registra el "No Me Gusta" (Dislike) y pasa a la siguiente biblioteca.
     */
    fun dislikeLibro() {
        viewModelScope.launch {
            // No se realiza ninguna acción de guardado, solo se transiciona.
            println("Libro descartado. Pasando a la siguiente biblioteca.")

            // 1. **Transicionar la Biblioteca**
            transicionarASiguienteBiblioteca()

            // 2. Cargar el siguiente libro
            cargarSiguienteLibro()
        }
    }

    // --- LÓGICA DE NAVEGACIÓN DE DATOS ---

    /**
     * Simula el paso a la siguiente biblioteca de otro usuario.
     */
    private fun transicionarASiguienteBiblioteca() {
        // En un entorno real, esto obtendría el ID del siguiente usuario recomendado
        // Por ahora, solo incrementamos un ID para simular un cambio de contexto.
        bibliotecaActualId += 1
        println("Cambiando a Biblioteca ID: $bibliotecaActualId")
    }

    /**
     * Carga el próximo libro de la 'bibliotecaActualId'.
     */
    private fun cargarSiguienteLibro() {
        viewModelScope.launch {
            isLoading = true

            // Simulación: El libro cargado depende de 'bibliotecaActualId'
            val newId = bibliotecaActualId * 100 + (0..10).random()

            // Aquí llamarías a: Repositorio.getLibroParaSwipe(bibliotecaActualId)

            kotlinx.coroutines.delay(500) // Simulación de carga

            // Crear un libro que refleje el cambio de usuario/biblioteca
            libroActual = Libro(
                id = newId.toLong(),
                titulo = "Libro de Usuario ${bibliotecaActualId}", // Título que cambia
                autor = "Autor X",
                portada = "",
                categoria = com.example.pantallas.modelos.Categoria(id = 1, nombre = "Nueva Categoría")
            )
            isLoading = false
        }
    }
}