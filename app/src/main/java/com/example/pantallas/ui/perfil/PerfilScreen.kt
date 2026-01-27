package com.example.pantallas.ui.perfil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.biblioteca.BibliotecaViewModel
import com.example.pantallas.ui.editarPerfil.EditarPerfil
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu
import kotlinx.coroutines.launch

class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PerfilAnyadir() }
    }
}

@Composable
fun PerfilAnyadir(
    perfilViewModel: PerfilViewModel = viewModel(),
    // 1. AÑADIMOS EL VIEWMODEL DE LA BIBLIOTECA
    bibliotecaViewModel: BibliotecaViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val state by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    // ---------------------------------------------------------
    // 1. RECUPERACIÓN DE ID SEGURA (SharedPreferences + Intent)
    // ---------------------------------------------------------
    val usuarioIdReal = remember {
        // A. Intentamos leer del Intent (prioridad alta)
        val idIntent = activity?.intent?.getLongExtra("USUARIO_ID", -1L) ?: -1L

        if (idIntent != -1L) {
            idIntent
        } else {
            // B. Si falla, leemos de la Memoria Persistente (Respaldo)
            context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getLong("ID_USUARIO_ACTUAL", -1L)
        }
    }

    // 2. CARGA DE DATOS (PERFIL + BIBLIOTECA)
    //En Perfil.kt
    // Usamos Unit para que solo cargue al "entrar", pero añadimos un refresco manual si hace falta
    // En Perfil.kt
    LaunchedEffect(state) {
        if (state== Lifecycle.State.RESUMED && usuarioIdReal != -1L) {
            // Ejecutamos las cargas
            perfilViewModel.cargarPerfilReal(usuarioIdReal)

            // El truco: limpiar la biblioteca visual antes de pedir la nueva
            // Esto evita que Compose piense que "ya tiene los datos"
            bibliotecaViewModel.cargarBibliotecaReal(usuarioIdReal)
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

        // --- CABECERA: TÍTULO Y EDITAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Perfil", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            // Botón Editar Perfil
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

        // --- TARJETA DE PERFIL ---
        if (perfilViewModel.estaCargando) {
            Box(modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            CardPerfil(
                perfil = perfilViewModel.perfil,
                foto = perfilViewModel.perfil.fotoPerfil
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- BOTÓN IR A BIBLIOTECA (Icono Libro) ---
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
                        val intent = Intent(context, Biblioteca::class.java)
                        intent.putExtra("USUARIO_ID", usuarioIdReal)
                        context.startActivity(intent)
                    }
            )
        }

        // --- CONTENIDO DE LA BIBLIOTECA (MODO LECTURA) ---
        Box(modifier = Modifier.fillMaxWidth()) {
            // 3. PASAMOS EL VIEWMODEL CARGADO AL COMPONENTE
            BibliotecaContenido(
                viewModel = bibliotecaViewModel,
                esModoEdicion = false
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------------------------------------------------
        // MENÚ INFERIOR
        // ---------------------------------------------------------
        Menu(context, usuarioIdReal)
    }
}