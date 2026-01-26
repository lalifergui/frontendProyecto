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

class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BibliotecaScreen() }
    }
}

// 1. CARD MODIFICADA: Recibe callbacks (onAdd, onDelete) en lugar de manejar lógica
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
                    onAddClick() // Llamamos al evento de añadir/cambiar
                }) {
                    Text(text = if (libro != null) "Cambiar" else "Añadir libro", color = Color(0xFF2196F3))
                }
            },
            dismissButton = {
                if (libro != null) {
                    TextButton(onClick = {
                        mostrarDialogoOpciones = false
                        onDeleteClick() // Llamamos al evento de borrar
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
            // Botón flotante "+"
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

// 2. SECCIÓN MODIFICADA: Pasa los eventos hacia arriba
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
            // Mostramos 3 huecos siempre
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

@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val bibliotecaData = viewModel.biblioteca

    // CONTROL DE SECCIÓN SELECCIONADA
    // Guardamos en qué sección pulsó el usuario para saber dónde poner el libro cuando vuelva
    var seccionSeleccionada by remember { mutableStateOf("") }

    // 3. LAUNCHER: Recibe el resultado de LibroScreen
    val launcherLibros = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Reconstruimos el objeto Libro desde el intent (o úsalo como Serializable)
            val id = data?.getLongExtra("LIBRO_ID", 0L) ?: 0L
            val titulo = data?.getStringExtra("LIBRO_TITULO") ?: ""
            val autor = data?.getStringExtra("LIBRO_AUTOR") ?: ""
            val catNombre = data?.getStringExtra("CATEGORIA_NOMBRE") ?: "General"

            if (titulo.isNotEmpty()) {
                val nuevoLibro = Libro(id, titulo, autor, "", Categoria(0, catNombre))
                // Añadimos al ViewModel
                viewModel.agregarLibroAMiBiblioteca(nuevoLibro, seccionSeleccionada)
            }
        }
    }

    // Alertas
    // La alerta obligatoria depende de si el ViewModel tiene libros
    var alertaObligatoriaVista by remember { mutableStateOf(false) } // Para que no salga todo el rato si ya la cerró una vez y sigue vacía
    val mostrarAlertaObligatoria = !viewModel.tieneLibros && !alertaObligatoriaVista

    var mostrarAlertaSalir by remember { mutableStateOf(false) }

    if (mostrarAlertaObligatoria) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("IMPORTANTE", color = Color.Red, fontWeight = FontWeight.Bold) },
            text = { Text("Tu biblioteca está vacía. Debes añadir AL MENOS un libro para continuar.") },
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
                    val intent = Intent(context, com.example.pantallas.ui.perfil.Perfil::class.java)
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

        // CONTENIDO
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5E7D3))
                    .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // SECCIÓN RECOMENDADOS
                    SeccionLibros(
                        titulo = "Recomendados",
                        libros = bibliotecaData.librosRecomendados,
                        esModoEdicion = true,
                        onAddLibro = {
                            seccionSeleccionada = "Recomendados"
                            val intent = Intent(context, LibroScreen::class.java)
                            launcherLibros.launch(intent)
                        },
                        onDeleteLibro = { libro -> viewModel.eliminarLibro(libro, "Recomendados") }
                    )
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))

                    // SECCIÓN ÚLTIMOS (LEIDOS)
                    SeccionLibros(
                        titulo = "Últimos libros",
                        libros = bibliotecaData.librosLeidos,
                        esModoEdicion = true,
                        onAddLibro = {
                            seccionSeleccionada = "Últimos libros"
                            val intent = Intent(context, LibroScreen::class.java)
                            launcherLibros.launch(intent)
                        },
                        onDeleteLibro = { libro -> viewModel.eliminarLibro(libro, "Últimos libros") }
                    )
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))

                    // SECCIÓN FUTURAS
                    SeccionLibros(
                        titulo = "Futuras lecturas",
                        libros = bibliotecaData.librosFuturasLecturas,
                        esModoEdicion = true,
                        onAddLibro = {
                            seccionSeleccionada = "Futuras lecturas"
                            val intent = Intent(context, LibroScreen::class.java)
                            launcherLibros.launch(intent)
                        },
                        onDeleteLibro = { libro -> viewModel.eliminarLibro(libro, "Futuras lecturas") }
                    )
                }
            }
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
                    // VALIDACIÓN ANTES DE GUARDAR
                    if (!viewModel.tieneLibros) {
                        alertaObligatoriaVista = false // Forzamos que salga la alerta
                    } else {
                        // AQUÍ GUARDARÍAS EN EL BACKEND (viewModel.guardarEnBackend())

                        // Navegación
                        val intent = Intent(context, com.example.pantallas.ui.perfil.Perfil::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        activity?.finish()
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Text("Guardar cambios", color = Color.White) }
        }
        Spacer(modifier = Modifier.height(30.dp))
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
            .background(Color(0xFFF5E7D3)) // Color crema de fondo
            .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // SECCIÓN RECOMENDADOS
            SeccionLibros(
                titulo = "Recomendados",
                libros = bibliotecaData.librosRecomendados,
                esModoEdicion = esModoEdicion,
                // Pasamos llaves vacías {} porque en el Perfil no se añade ni borra aquí
                onAddLibro = {},
                onDeleteLibro = {}
            )

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

            // SECCIÓN ÚLTIMOS
            SeccionLibros(
                titulo = "Últimos libros",
                libros = bibliotecaData.librosLeidos,
                esModoEdicion = esModoEdicion,
                onAddLibro = {},
                onDeleteLibro = {}
            )

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

            // SECCIÓN FUTURAS
            SeccionLibros(
                titulo = "Futuras lecturas",
                libros = bibliotecaData.librosFuturasLecturas,
                esModoEdicion = esModoEdicion,
                onAddLibro = {},
                onDeleteLibro = {}
            )
        }
    }
}