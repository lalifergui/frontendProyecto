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
        setContent { PerfilAnyadir() }
    }
}

@Composable
fun PerfilAnyadir(perfilViewModel: PerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val perfilData = perfilViewModel.perfil
    val activity = (context as? Activity)
    val esNuevoRegistro = remember { activity?.intent?.getBooleanExtra("NUEVO_REGISTRO", false) ?: false }
    var mostrarAviso by remember { mutableStateOf(esNuevoRegistro) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Perfil", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Perfil",
                modifier = Modifier.size(36.dp).clickable {
                    context.startActivity(Intent(context, EditarPerfil::class.java))
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
        CardPerfil(perfil = perfilData)

        Spacer(modifier = Modifier.height(25.dp))

        // --- Sección de Biblioteca ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = "Ir a Biblioteca",
                modifier = Modifier.size(36.dp).clickable {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPerfilScreen() { PerfilAnyadir() }