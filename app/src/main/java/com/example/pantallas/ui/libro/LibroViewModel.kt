import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.repository.LibroRepositorio
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {

    private val repositorio = LibroRepositorio()

    // 🎯 Lista de libros de ejemplo iniciales
    private val librosEjemplo = listOf(
        Libro(1, "Donde los Árboles Cantan", "Laura Gallego", "", Categoria(1, "Fantasía")),
        Libro(2, "El nombre del viento", "Patrick Rothfuss", "", Categoria(1, "Fantasía")),
        Libro(3, "Reina Roja", "Juan Gómez-Jurado", "", Categoria(2, "Thriller")),
        Libro(
            4,
            "Cien años de soledad",
            "Gabriel García Márquez",
            "",
            Categoria(3, "Realismo Mágico")
        ),
        Libro(5, "El resplandor", "Stephen King", "", Categoria(4, "Terror")),
        Libro(6, "Hola", "Laura", "", Categoria(4, "Terror")),
        Libro(7, "Hola", "Sandra", "", Categoria(1, "Fantasía"))
    )

    // Inicializamos con la lista de ejemplo para que no aparezca vacía al cargar
    private val _libros = MutableStateFlow<List<Libro>>(librosEjemplo)
    val libros: StateFlow<List<Libro>> get() = _libros

    init {
        obtenerLibros()
    }

    fun obtenerLibros() {
        viewModelScope.launch {
            try {
                val listaServidor = repositorio.getLibros()
                // Combinamos los ejemplos con los del servidor
                _libros.value = librosEjemplo + listaServidor
            } catch (e: Exception) {
                // Si falla el servidor, al menos se quedan los ejemplos
                println("Error al obtener libros: ${e.message}")
            }
        }
    }

    fun agregarLibro(titulo: String, autor: String, portada: String, categoria: Categoria) {
        val nuevoLibro = Libro(
            id = 0,
            titulo = titulo,
            autor = autor,
            portada = portada,
            categoria = categoria
        )

        viewModelScope.launch {
            try {
                val libroCreado = repositorio.crearLibro(nuevoLibro)
                _libros.value = _libros.value + libroCreado
            } catch (e: Exception) {
                println("Error al agregar libro: ${e.message}")
            }
        }
    }
}