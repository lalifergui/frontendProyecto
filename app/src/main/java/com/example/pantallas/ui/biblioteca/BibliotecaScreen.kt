package com.example.pantallas.ui.biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Libro

class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaScreen()
        }
    }
}

@Composable
fun LibroCard(libroInicial: Libro?) {
    var libro by remember { mutableStateOf(libroInicial) }
    var mostrarDialogoOpciones by remember { mutableStateOf(false) }

    // --- VENTANA EMERGENTE (DIALOGO DE OPCIONES) ---
    if (mostrarDialogoOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoOpciones = false },
            title = {
                Text(text = if (libro != null) "Opciones del libro" else "Espacio vacío")
            },
            text = {
                Text(text = if (libro != null) "¿Qué desea hacer con '${libro!!.titulo}'?" else "¿Desea añadir un nuevo libro a este hueco?")
            },
            confirmButton = {
                if (libro != null) {
                    // Opción Editar
                    TextButton(onClick = { /* Lógica editar */ mostrarDialogoOpciones = false }) {
                        Text("Editar libro", color = Color(0xFF2196F3))
                    }
                } else {
                    // Opción Añadir
                    TextButton(onClick = { /* Lógica añadir */ mostrarDialogoOpciones = false }) {
                        Text("Añadir libro")
                    }
                }
            },
            dismissButton = {
                if (libro != null) {
                    // Opción Eliminar
                    TextButton(onClick = {
                        libro = null
                        mostrarDialogoOpciones = false
                    }) {
                        Text("Eliminar", color = Color.Red)
                    }
                } else {
                    TextButton(onClick = { mostrarDialogoOpciones = false }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .width(90.dp)
            .height(115.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Título del libro si existe
            if (libro != null) {
                Text(
                    text = libro!!.titulo,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp)
                )
            }

            // Botón "+" para abrir el diálogo
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (libro == null) Alignment.Center else Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { mostrarDialogoOpciones = true },
                    modifier = Modifier
                        .size(if (libro == null) 40.dp else 24.dp)
                        .background(
                            if (libro == null) Color.Transparent else Color.White.copy(alpha = 0.6f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Abrir opciones",
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = libro?.categoria?.nombre ?: "Sin asignar",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (libro == null) Color.LightGray else Color.DarkGray,
            maxLines = 1
        )
    }
}

@Composable
fun SeccionLibros(titulo: String, libros: List<Libro>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Dibujamos siempre 3 huecos
            for (i in 0 until 3) {
                val libroActual = libros.getOrNull(i)
                LibroCard(libroInicial = libroActual)
            }
        }
    }
}

@Composable
fun BibliotecaContenido(viewModel: BibliotecaViewModel = viewModel()) {
    val bibliotecaData = viewModel.biblioteca
    val ColorExteriorEstanteria = Color(0xFFF5E7D3)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorExteriorEstanteria)
            .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SeccionLibros("Recomendados", bibliotecaData.librosRecomendados)
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            SeccionLibros("Últimos libros", bibliotecaData.librosLeidos)
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            SeccionLibros("Futuras lecturas", bibliotecaData.librosFuturasLecturas)
        }
    }
}

@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edita tu\nBiblioteca",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            BibliotecaContenido(viewModel = viewModel)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBibliotecaScreen() {
    BibliotecaScreen()
}