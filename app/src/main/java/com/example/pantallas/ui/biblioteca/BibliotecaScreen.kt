package com.example.pantallas.ui.biblioteca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro
import com.example.pantallas.ui.libro.LibroScreen
import com.example.pantallas.ui.perfil.Perfil // Asegúrate de importar tu Perfil correctamente

class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BibliotecaScreen() }
    }
}

// 1. TARJETA DE LIBRO
@Composable
fun LibroCard(
    libro: Libro?,
    esModoEdicion: Boolean,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var mostrarDialogoOpciones by remember { mutableStateOf(false) }

    if (mostrarDialogoOpciones && esModoEdicion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoOpciones = false },
            title = { Text(text = if (libro != null) "Opciones" else "Hueco vacío") },
            text = { Text(text = if (libro != null) "¿Qué desea hacer con este libro?" else "¿Desea añadir un libro?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoOpciones = false
                    onAddClick()
                }) {
                    Text(text = if (libro != null) "Cambiar" else "Añadir libro", color = Color(0xFF2196F3))
                }
            },
            dismissButton = {
                if (libro != null) {
                    TextButton(onClick = {
                        mostrarDialogoOpciones = false
                        onDeleteClick()
                    }) {
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
                .background(if (libro == null) Color(0xFFF0F0F0) else Color.LightGray, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (libro != null) {
                Text(
                    text = libro.titulo,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp)
                )
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
                            .background(if (libro == null) Color.Transparent else Color.White.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = libro?.categoria?.nombre ?: "Vacío",
            fontSize = 10.sp,
            color = if(libro != null) Color(0xFF2196F3) else Color.Gray,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

// 2. SECCIÓN DE LIBROS
@Composable
fun SeccionLibros(
    titulo: String,
    libros: List<Libro>,
    esModoEdicion: Boolean,
    onAddLibro: () -> Unit,
    onDeleteLibro: (Libro) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0 until 3) {
                val libroActual = libros.getOrNull(i)
                LibroCard(
                    libro = libroActual,
                    esModoEdicion = esModoEdicion,
                    onAddClick = { onAddLibro() },
                    onDeleteClick = { if (libroActual != null) onDeleteLibro(libroActual) }
                )
            }
        }
    }
}

// 3. PANTALLA PRINCIPAL
@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)

    // RECUPERAR ID DEL USUARIO
    val usuarioId = remember {
        activity?.intent?.getLongExtra("USUARIO_ID", -1L) ?: -1L
    }

    // CARGA INICIAL DE DATOS
    LaunchedEffect(Unit) {
        if (usuarioId != -1L) {
            viewModel.cargarBibliotecaReal(usuarioId)
        }
    }

    var seccionSeleccionada by remember { mutableStateOf("") }

    // LAUNCHER PARA AGREGAR LIBROS
    val launcherLibros = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val id = data?.getLongExtra("LIBRO_ID", 0L) ?: 0L
            val titulo = data?.getStringExtra("LIBRO_TITULO") ?: ""
            val autor = data?.getStringExtra("LIBRO_AUTOR") ?: ""
            val catNombre = data?.getStringExtra("CATEGORIA_NOMBRE") ?: "General"

            if (titulo.isNotEmpty()) {
                val nuevoLibro = Libro(id, titulo, autor, "", Categoria(0, catNombre))
                viewModel.agregarLibroAMiBiblioteca(nuevoLibro, seccionSeleccionada)
            }
        }
    }

    // ALERTAS
    var alertaObligatoriaVista by remember { mutableStateOf(false) }
    val mostrarAlertaObligatoria = !viewModel.tieneLibros && !alertaObligatoriaVista
    var mostrarAlertaSalir by remember { mutableStateOf(false) }

    if (mostrarAlertaObligatoria) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("IMPORTANTE", color = Color.Red, fontWeight = FontWeight.Bold) },
            text = { Text("Tu biblioteca está vacía. Añade al menos un libro.") },
            confirmButton = {
                Button(onClick = { alertaObligatoriaVista = true }) { Text("Entendido") }
            }
        )
    }

    if (mostrarAlertaSalir) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaSalir = false },
            title = { Text("Atención") },
            text = { Text("Si sales ahora perderás los cambios no guardados.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarAlertaSalir = false
                    val intent = Intent(context, Perfil::class.java)
                    intent.putExtra("USUARIO_ID", usuarioId)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    activity?.finish()
                }) { Text("SALIR", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAlertaSalir = false }) { Text("CANCELAR") }
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
        Text("Edita tu\nBiblioteca", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 24.dp))

        // CONTENIDO EDITABLE
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            BibliotecaContenido(
                viewModel = viewModel,
                esModoEdicion = true,
                onAddLibro = { seccion ->
                    seccionSeleccionada = seccion
                    val intent = Intent(context, LibroScreen::class.java)
                    launcherLibros.launch(intent)
                },
                onDeleteLibro = { libro, seccion ->
                    viewModel.eliminarLibro(libro, seccion)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONES
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { mostrarAlertaSalir = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Salir") }

            Button(
                onClick = {
                    if (!viewModel.tieneLibros) {
                        alertaObligatoriaVista = false
                    } else {
                        // AQUÍ ES DONDE SE LLAMA AL SERVIDOR
                        viewModel.guardarCambiosEnServidor(usuarioId) {
                            val intent = Intent(context, Perfil::class.java).apply {
                                putExtra("USUARIO_ID", usuarioId) // Pasamos el ID de vuelta
                                //evitamos que se cree una pila infinita de pantallas
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            }

                            context.startActivity(intent)
                            activity?.finish()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = !viewModel.guardando, // Deshabilita si está guardando
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                if (viewModel.guardando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar cambios", color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

// 4. CONTENIDO COMPARTIDO (Se usa aquí y en Perfil)
@Composable
fun BibliotecaContenido(
    viewModel: BibliotecaViewModel = viewModel(),
    esModoEdicion: Boolean = false,
    onAddLibro: (String) -> Unit = {},
    onDeleteLibro: (Libro, String) -> Unit = { _, _ -> }
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

            // RECOMENDADOS
            SeccionLibros(
                titulo = "Recomendados",
                libros = bibliotecaData.librosRecomendados,
                esModoEdicion = esModoEdicion,
                onAddLibro = { onAddLibro("Recomendados") },
                onDeleteLibro = { libro -> onDeleteLibro(libro, "Recomendados") }
            )

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

            // ÚLTIMOS
            SeccionLibros(
                titulo = "Últimos libros",
                libros = bibliotecaData.librosLeidos,
                esModoEdicion = esModoEdicion,
                onAddLibro = { onAddLibro("Últimos libros") },
                onDeleteLibro = { libro -> onDeleteLibro(libro, "Últimos libros") }
            )

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

            // FUTURAS
            SeccionLibros(
                titulo = "Futuras lecturas",
                libros = bibliotecaData.librosFuturasLecturas,
                esModoEdicion = esModoEdicion,
                onAddLibro = { onAddLibro("Futuras lecturas") },
                onDeleteLibro = { libro -> onDeleteLibro(libro, "Futuras lecturas") }
            )
        }
    }
}