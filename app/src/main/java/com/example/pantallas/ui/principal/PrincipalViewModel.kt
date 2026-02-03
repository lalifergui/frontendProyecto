package com.example.pantallas.ui.principal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.model.BibliotecaDTO
import com.example.pantallas.data.model.LibroDTO
import com.example.pantallas.data.model.PerfilDTO
import com.example.pantallas.data.network.RetrofitClient
import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Perfil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PerfilUsuarioSugerido(
    val perfil: Perfil,
    val biblioteca: Biblioteca
)

class PrincipalViewModel : ViewModel() {

    private val api = RetrofitClient.usuarioApi

    var usuarioSugerido: PerfilUsuarioSugerido? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    private var idsParaExplorar = mutableListOf<Long>()

    // 1. CARGAR LISTA INICIAL DE EXPLORACIÓN
    fun cargarExploracion(miId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val response = api.getAllUsuarios()
                if (response.isSuccessful && response.body() != null) {
                    val listaIds = response.body()!!
                        .mapNotNull { it.id }
                        .filter { it != miId } // Excluye al usuario actual
                        .toMutableList()

                    listaIds.shuffle()
                    idsParaExplorar = listaIds

                    withContext(Dispatchers.Main) {
                        cargarSiguientePerfil()
                    }
                } else {
                    withContext(Dispatchers.Main) { isLoading = false }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { isLoading = false }
                e.printStackTrace()
            }
        }
    }

    // 2. CARGAR EL SIGUIENTE PERFIL DE LA LISTA
    fun cargarSiguientePerfil() {
        if (idsParaExplorar.isEmpty()) {
            usuarioSugerido = null
            isLoading = false
            return
        }

        // Sacamos el primer ID de la lista barajada
        val siguienteId = idsParaExplorar.removeAt(0)

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val perfilRes = api.getPerfil(siguienteId)
                val biblioRes = api.getBiblioteca(siguienteId)

                if (perfilRes.isSuccessful && biblioRes.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        //  CAMBIO CRÍTICO: Pasamos el ID real del usuario al mapeador
                        usuarioSugerido = PerfilUsuarioSugerido(
                            perfil = pDTOaModelo(perfilRes.body()!!, siguienteId),
                            biblioteca = bDTOaModelo(biblioRes.body()!!)
                        )
                        isLoading = false
                    }
                } else {
                    // Si falla la carga de un usuario específico, intentamos con el siguiente
                    withContext(Dispatchers.Main) { cargarSiguientePerfil() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { cargarSiguientePerfil() }
            }
        }
    }

    // 3. FUNCIÓN PARA DAR "LIKE" (Corazón Verde)
    fun darLike(miId: Long, favoritoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Petición al servidor (Ruta: usuarios/{id}/favoritos/{favoritoId})
                val response = api.addFavorito(miId, favoritoId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // 🎯 2. Éxito: Pasamos al siguiente perfil
                        println("DEBUG: Favorito guardado con éxito para usuario: $favoritoId")
                        descartar()
                    } else {
                        // Si el servidor da error (ej: 500 por recursión), lo veremos aquí
                        println("DEBUG: Error del servidor (${response.code()}). Verifica el hashCode en el backend.")
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: Error de red: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun descartar() {
        cargarSiguientePerfil()
    }

    // --- MAPEADORES (DTO -> MODELO UI) ---

    // 🎯 Mapeador corregido para inyectar el ID de Usuario en el modelo Perfil
    private fun pDTOaModelo(dto: PerfilDTO, idUsuarioReal: Long) = Perfil(
        perfil_id = idUsuarioReal, // Forzamos que perfil_id sea el ID que reconoce la tabla Usuarios
        nombre = dto.nombre,
        apellidos = dto.apellidos,
        fechaNacimiento = dto.fechaNacimiento,
        ciudad = dto.ciudad,
        fotoPerfil = dto.fotoPerfil ?: ""
    )

    private fun bDTOaModelo(dto: BibliotecaDTO) = Biblioteca(
        id = 0,
        usuario = null,
        librosRecomendados = dto.recomendados.map { convertirLibro(it) },
        librosLeidos = dto.leidos.map { convertirLibro(it) },
        librosFuturasLecturas = dto.futurasLecturas.map { convertirLibro(it) }
    )

    private fun convertirLibro(dto: LibroDTO) = Libro(
        id = dto.id ?: 0L,
        titulo = dto.titulo,
        autor = dto.autor,
        portada = dto.portada ?: "",
        categoria = Categoria(0, dto.categoriaNombre ?: "General")
    )
}