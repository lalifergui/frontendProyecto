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
import kotlinx.coroutines.delay

class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerfilAnyadir()
        }
    }
}

@Composable
fun PerfilAnyadir(
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val perfilData = perfilViewModel.perfil

    // --- LÓGICA DE NAVEGACIÓN Y AVISO DE BIENVENIDA ---
    // Recuperamos el Intent de la Activity actual
    val activity = (context as? Activity)
    val esNuevoRegistro = remember {
        activity?.intent?.getBooleanExtra("NUEVO_REGISTRO", false) ?: false
    }

    // Estado para controlar la visibilidad de la alerta emergente
    var mostrarAvisoBiblioteca by remember { mutableStateOf(esNuevoRegistro) }

    if (mostrarAvisoBiblioteca) {
        AlertDialog(
            onDismissRequest = { /* No se cierra al tocar fuera para forzar el aviso */ },
            title = {
                Text(
                    text = "¡IMPORTANTE!",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Debes rellenar tu Biblioteca.\nRedirigiendo en 5 segundos..."
                )
            },
            confirmButton = {} // Se omite el botón para usar el temporizador
        )

        // Temporizador de 5 segundos para la redirección automática
        LaunchedEffect(Unit) {
            delay(5000)
            mostrarAvisoBiblioteca = false

            // Navegamos a la pantalla de Editar Biblioteca
            val intent = Intent(context, Biblioteca::class.java)
            context.startActivity(intent)

            // Limpiamos el extra para evitar que el diálogo se repita al girar la pantalla
            activity?.intent?.removeExtra("NUEVO_REGISTRO")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // --- Título y Botones de Acción ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Perfil",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // ICONO 1: Editar Perfil (Lápiz)
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar Perfil",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            val intent = Intent(context, EditarPerfil::class.java)
                            context.startActivity(intent)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Card Perfil (Componente Reutilizable) ---
        CardPerfil(perfil = perfilData)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Sección de Biblioteca ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = "Editar Biblioteca",
                tint = Color.Black,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        val intent = Intent(context, Biblioteca::class.java)
                        context.startActivity(intent)
                    }
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            // Contenido dinámico de la biblioteca
            BibliotecaContenido()
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menú de navegación inferior
        Menu(context)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPerfilScreen() {
    PerfilAnyadir()
}