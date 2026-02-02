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

    // 1. CARGAR LISTA (Cambio: Corregida sintaxis Dispatchers)
    fun cargarExploracion(miId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val response = api.getAllUsuarios() // Endpoint en UsuarioController
                if (response.isSuccessful && response.body() != null) {
                    val listaIds = response.body()!!
                        .mapNotNull { it.id }
                        .filter { it != miId } // Excluye a Laura (ID 1)
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
            }
        }
    }

    // 2. CARGAR SIGUIENTE (Cambio: Ahora descarga info real de MySQL)
    fun cargarSiguientePerfil() {
        if (idsParaExplorar.isEmpty()) {
            usuarioSugerido = null
            isLoading = false
            return
        }

        val siguienteId = idsParaExplorar.removeAt(0)

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val perfilRes = api.getPerfil(siguienteId) //
                val biblioRes = api.getBiblioteca(siguienteId) //

                if (perfilRes.isSuccessful && biblioRes.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        usuarioSugerido = PerfilUsuarioSugerido(
                            perfil = pDTOaModelo(perfilRes.body()!!),
                            biblioteca = bDTOaModelo(biblioRes.body()!!)
                        )
                        isLoading = false
                    }
                } else {
                    cargarSiguientePerfil()
                }
            } catch (e: Exception) {
                cargarSiguientePerfil()
            }
        }
    }

    // 3. DAR LIKE (Cambio: Llama a la tabla de favoritos de la DB)
    fun darLike(miId: Long, favoritoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //  1. Petición al servidor
                val response = RetrofitClient.usuarioApi.addFavorito(miId, favoritoId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // 🎯 2. Solo si el servidor guarda, pasamos al siguiente perfil
                        descartar()
                        println("DEBUG: Guardado con éxito")
                    } else {
                        println("DEBUG: Error del servidor: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun descartar() {
        cargarSiguientePerfil()
    }

    // Mapeadores (DTO -> Modelo)
    private fun pDTOaModelo(dto: PerfilDTO) = Perfil(
        perfil_id = dto.perfilId ?: 0L,
        nombre = dto.nombre,
        apellidos = dto.apellidos,
        fechaNacimiento = dto.fechaNacimiento,
        ciudad = dto.ciudad,
        fotoPerfil = dto.fotoPerfil
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