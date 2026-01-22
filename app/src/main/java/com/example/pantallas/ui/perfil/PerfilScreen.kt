package com.example.pantallas.ui.perfil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.editarPerfil.EditarPerfil
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu

class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PerfilAnyadir() }
    }
}

@Composable
fun PerfilAnyadir(perfilViewModel: PerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val state by androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val activity = (context as? Activity)
    val usuarioIdReal = remember { activity?.intent?.getLongExtra("USUARIO_ID", 1L) ?: 1L }

    // 1. CARGA DINÁMICA: Cuando se inicia la pantalla, pedimos los datos al servidor
    LaunchedEffect(state) {
        if (state == androidx.lifecycle.Lifecycle.State.RESUMED) {
            perfilViewModel.cargarPerfilReal(usuarioId = usuarioIdReal) //Usa aquí tu ID dinámico
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Perfil", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Perfil",
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        val intent = Intent(context, EditarPerfil::class.java).apply {

                            putExtra("Edicion", true)
                            putExtra("USUARIO_ID", usuarioIdReal)
                        }

                        context.startActivity(intent)
                    }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // 2. ESTADO DE CARGA: Si el ViewModel está trabajando, mostramos un indicador
        if (perfilViewModel.estaCargando) {
            Box(modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            // 3. DATOS REALES: Mostramos la Card con la información que devolvió MySQL
            CardPerfil(perfil = perfilViewModel.perfil)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- Sección de Biblioteca ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = "Ir a Biblioteca",
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        context.startActivity(Intent(context, Biblioteca::class.java))
                    }
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            BibliotecaContenido(esModoEdicion = false)
        }

        Menu(context)
    }
}

/**
 * @Preview(showBackground = true, showSystemUi = true)
 * @Composable
 * fun PreviewPerfilScreen() {
 *     // Creamos un ViewModel "falso" o simplemente no llamamos a la carga real en el preview
 *     val vm = PerfilViewModel()
 *     // No llamamos a cargarPerfilReal aquí para que el Preview no crashee
 *     PerfilAnyadir(perfilViewModel = vm)
 * }
 */
