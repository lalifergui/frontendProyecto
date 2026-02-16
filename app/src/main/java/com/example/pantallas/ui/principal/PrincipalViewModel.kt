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

    // Carga inicial aleatoria (aquí sí usamos shuffle)
    fun cargarExploracion(miId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val response = api.getAllUsuarios()
                if (response.isSuccessful && response.body() != null) {
                    val listaIds = response.body()!!
                        .mapNotNull { it.id }
                        .filter { it != miId }

                    val listaMutable = listaIds.toMutableList()
                    listaMutable.shuffle() // Aleatorio para la exploración general
                    idsParaExplorar = listaMutable

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

    // Filtrado inteligente por categoría
    fun filtrarPorCategoria(categoria: String, miId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                // 🎯 Llamamos al endpoint de Sandra que ya devuelve la lista ORDENADA
                val response = api.buscarPorCategoria(categoria)

                if (response.isSuccessful && response.body() != null) {
                    val listaIds = response.body()!!
                        .mapNotNull { it.id }
                        .filter { it != miId }
                        .toMutableList()

                    // ❌ ELIMINADO listaIds.shuffle()
                    // Mantenemos el orden del servidor: Jose(3) -> Sandra(1) -> Juan(0)
                    idsParaExplorar = listaIds

                    withContext(Dispatchers.Main) {
                        cargarSiguientePerfil()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    fun cargarSiguientePerfil() {
        if (idsParaExplorar.isEmpty()) {
            usuarioSugerido = null
            isLoading = false
            return
        }

        val siguienteId = idsParaExplorar.removeAt(0)

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val perfilRes = api.getPerfil(siguienteId)
                val biblioRes = api.getBiblioteca(siguienteId)

                if (perfilRes.isSuccessful && biblioRes.isSuccessful) {
                    val pDto = perfilRes.body()
                    val bDto = biblioRes.body()

                    if (pDto != null && bDto != null) {
                        withContext(Dispatchers.Main) {
                            usuarioSugerido = PerfilUsuarioSugerido(
                                perfil = pDTOaModelo(pDto, siguienteId),
                                biblioteca = bDTOaModelo(bDto)
                            )
                            isLoading = false
                        }
                    } else {
                        withContext(Dispatchers.Main) { cargarSiguientePerfil() }
                    }
                } else {
                    withContext(Dispatchers.Main) { cargarSiguientePerfil() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { cargarSiguientePerfil() }
            }
        }
    }

    fun darLike(miId: Long, favoritoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.addFavorito(miId, favoritoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        println("DEBUG: Favorito guardado con éxito para usuario: $favoritoId")
                        descartar()
                    } else {
                        println("DEBUG: Error del servidor (${response.code()})")
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


    private fun pDTOaModelo(dto: PerfilDTO, idUsuarioReal: Long) = Perfil(
        perfil_id = idUsuarioReal,
        nombre = dto.nombre ?: "Usuario",
        apellidos = dto.apellidos ?: "",
        fechaNacimiento = dto.fechaNacimiento ?: "2000-01-01",
        ciudad = dto.ciudad ?: "Desconocida",
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
        titulo = dto.titulo ?: "Sin título",
        autor = dto.autor ?: "Autor desconocido",
        portada = dto.portada ?: "",
        categoria = Categoria(0, dto.categoriaNombre ?: "General")
    )
}