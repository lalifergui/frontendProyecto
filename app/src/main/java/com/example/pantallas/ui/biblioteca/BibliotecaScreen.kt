package com.example.pantallas.ui.biblioteca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Libro
import com.example.pantallas.modelos.Perfil

import com.example.pantallas.ui.libro.LibroScreen

class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuarioId = intent.getLongExtra("USUARIO_ID",-1L)
        enableEdgeToEdge()
        setContent { BibliotecaScreen() }
    }
}

@Composable
fun LibroCard(libroInicial: Libro?, esModoEdicion: Boolean) {

    val context = LocalContext.current
    var libro by remember { mutableStateOf(libroInicial) }
    var mostrarDialogoOpciones by remember { mutableStateOf(false) }

    if (mostrarDialogoOpciones && esModoEdicion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoOpciones = false },
            title = { Text(text = if (libro != null) "Opciones del libro" else "Hueco vacío") },
            text = { Text(text = if (libro != null) "¿Qué desea hacer?" else "¿Desea añadir un libro?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoOpciones = false
                    val intent = Intent(context, LibroScreen::class.java)
                    // Añade este flag para asegurar que la actividad se lance correctamente
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }) {
                    Text(
                        text = if (libro != null) "Editar / Cambiar" else "Añadir libro",
                        color = Color(0xFF2196F3)
                    )
                }
            },
            dismissButton = {
                if (libro != null) {
                    TextButton(onClick = { libro = null; mostrarDialogoOpciones = false }) {
                        Text("Eliminar", color = Color.Red)
                    }
                } else {
                    TextButton(onClick = { mostrarDialogoOpciones = false }) { Text("Cancelar") }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .width(95.dp)
            .height(130.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    if (libro == null) Color(0xFFF0F0F0) else Color.LightGray,
                    RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (libro != null) {
                libro?.let { datosLibros ->
                    Text(
                        text = datosLibros.titulo,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp)
                    )
                }

            }

            if (esModoEdicion) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (libro == null) Alignment.Center else Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { mostrarDialogoOpciones = true },
                        modifier = Modifier
                            .size(if (libro == null) 40.dp else 24.dp)
                            .background(
                                if (libro == null) Color.Transparent else Color.White.copy(
                                    alpha = 0.6f
                                ), CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = libro?.categoria?.nombre ?: "Vacío", fontSize = 10.sp,color = if(libro != null) Color(0xFF2196F3) else Color.Gray,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1)
    }
}

@Composable
fun SeccionLibros(titulo: String, libros: List<Libro>, esModoEdicion: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0 until 3) {
                LibroCard(libroInicial = libros.getOrNull(i), esModoEdicion = esModoEdicion)
            }
        }
    }
}

@Composable
fun BibliotecaContenido(
    viewModel: BibliotecaViewModel = viewModel(),
    esModoEdicion: Boolean = false
) {
    val bibliotecaData = viewModel.biblioteca
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5E7D3))
            .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SeccionLibros("Recomendados", bibliotecaData.librosRecomendados, esModoEdicion)
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            SeccionLibros("Últimos libros", bibliotecaData.librosLeidos, esModoEdicion)
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            SeccionLibros("Futuras lecturas", bibliotecaData.librosFuturasLecturas, esModoEdicion)
        }
    }
}

@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)

    // ESTADOS
    var mostrarAlertaObligatoria by remember { mutableStateOf(true) }
    var mostrarAlertaSalir by remember { mutableStateOf(false) }

    // --- ALERTA 1: OBLIGATORIA AL ENTRAR ---
    if (mostrarAlertaObligatoria) {
        AlertDialog(
            onDismissRequest = { }, // No se cierra tocando fuera
            title = { Text("IMPORTANTE", color = Color.Red, fontWeight = FontWeight.Bold) },
            text = { Text("Debes rellenar la biblioteca con AL MENOS un libro.") },
            confirmButton = {
                Button(onClick = { mostrarAlertaObligatoria = false }) {
                    Text("Entendido")
                }
            }
        )
    }


    if (mostrarAlertaSalir) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaSalir = false },
            title = { Text("Atención", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que quieres salir sin guardar los cambios?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAlertaSalir = false

                        val intent =
                            Intent(context, com.example.pantallas.ui.perfil.Perfil::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        activity?.finish()
                    }
                ) {
                    Text("SÍ, SALIR", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAlertaSalir = false }) {
                    Text("NO, QUEDARME")
                }
            }
        )
    }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            BibliotecaContenido(viewModel = viewModel, esModoEdicion = true)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTONES INFERIORES ---
        // --- BOTONES INFERIORES ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón Salir (Este ya lo tenías bien)
            OutlinedButton(
                onClick = { mostrarAlertaSalir = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salir")
            }

            // Botón Guardar (MODIFICADO AQUÍ)
            Button(
                onClick = {
                    //CAMBIO: Asegúrate de importar com.example.pantallas.ui.perfil.Perfil
                    val intent = Intent(context, com.example.pantallas.ui.perfil.Perfil::class.java)

                    // Estas flags limpian la pila para que no puedas volver atrás con el botón del móvil
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    context.startActivity(intent)
                    activity?.finish()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Guardar cambios", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBibliotecaScreen() {
    BibliotecaScreen()
}