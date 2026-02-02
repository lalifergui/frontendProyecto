package com.example.pantallas.ui.perfil

import android.app.Activity
import android.content.Context
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.biblioteca.BibliotecaViewModel
import com.example.pantallas.ui.editarPerfil.EditarPerfil
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu

class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Identificamos al usuario: ¿soy yo o es otro?
        val idAVisualizar = intent.getLongExtra("USUARIO_ID", -1L)
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val miIdReal = sharedPref.getLong("ID_USUARIO_ACTUAL", -1L)

        // Lógica de autoría: si el ID es el mío o no viene ninguno (-1), es mi perfil
        val esMiPropioPerfil = (idAVisualizar == miIdReal || idAVisualizar == -1L)
        val idFinal = if (idAVisualizar == -1L) miIdReal else idAVisualizar

        setContent {
            PerfilAnyadir(
                usuarioId = idFinal,
                modoEdicionHabilitado = esMiPropioPerfil
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilAnyadir(
    usuarioId: Long,
    modoEdicionHabilitado: Boolean,
    perfilViewModel: PerfilViewModel = viewModel(),
    bibliotecaViewModel: BibliotecaViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    // Carga de datos al entrar o resumir la pantalla para asegurar frescura
    LaunchedEffect(state) {
        if (state == Lifecycle.State.RESUMED && usuarioId != -1L) {
            perfilViewModel.cargarPerfilReal(usuarioId)
            bibliotecaViewModel.cargarBibliotecaReal(usuarioId)
        }
    }

    Scaffold(
        topBar = {
            // BARRA SUPERIOR: Solo aparece si estamos viendo a OTRA PERSONA
            if (!modoEdicionHabilitado) {
                TopAppBar(
                    title = { Text("Volver a favoritos") },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) { // Cerramos la Activity para volver atrás
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        },
        bottomBar = {
            // MENÚ INFERIOR: Solo aparece si es MI PERFIL
            if (modoEdicionHabilitado) {
                Menu(context, usuarioId)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- TÍTULO Y BOTÓN EDITAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (modoEdicionHabilitado) "Mi Perfil" else "Perfil ajeno",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                if (modoEdicionHabilitado) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar Perfil",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                val intent = Intent(context, EditarPerfil::class.java).apply {
                                    putExtra("USUARIO_ID", usuarioId)
                                    putExtra("Edicion", true)
                                }
                                context.startActivity(intent)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // --- DATOS DEL PERFIL ---
            if (perfilViewModel.estaCargando) {
                CircularProgressIndicator(modifier = Modifier.padding(20.dp))
            } else {
                CardPerfil(
                    perfil = perfilViewModel.perfil,
                    foto = perfilViewModel.perfil.fotoPerfil
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- SECCIÓN BIBLIOTECA ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (modoEdicionHabilitado) "Mi Biblioteca" else "Biblioteca",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // El icono de gestionar biblioteca solo sale si soy el dueño
                if (modoEdicionHabilitado) {
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = "Ir a Biblioteca",
                        modifier = Modifier.size(32.dp).clickable {
                            val intent = Intent(context, Biblioteca::class.java).apply {
                                putExtra("USUARIO_ID", usuarioId)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }

            // Biblioteca en modo lectura (esModoEdicion = false siempre aquí)
            Box(modifier = Modifier.fillMaxWidth()) {
                BibliotecaContenido(viewModel = bibliotecaViewModel, esModoEdicion = false)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}