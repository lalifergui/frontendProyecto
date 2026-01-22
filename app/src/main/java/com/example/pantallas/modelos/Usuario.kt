import com.example.pantallas.modelos.Biblioteca
import com.example.pantallas.modelos.Perfil

// com.example.pantallas.modelos.Usuario.kt
data class Usuario(
    val usuario_id: Long, // Debe coincidir con el nombre en Java
    val email: String,
    val password: String,
    // Usamos DTOs o los modelos correspondientes
    val perfil: Perfil? = null,
    val biblioteca: Biblioteca? = null
)