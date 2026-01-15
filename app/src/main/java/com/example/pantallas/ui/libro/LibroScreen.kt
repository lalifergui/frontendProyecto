package com.example.pantallas.ui.libro

import LibroViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro

class LibroScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    viewModel: LibroViewModel = viewModel()
) {
    PantallaLibros(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros(libroViewModel: LibroViewModel) {
    val context = LocalContext.current
    val libros by libroViewModel.libros.collectAsState()

    // ESTADOS DE BÚSQUEDA Y DIÁLOGO
    var estado by remember { mutableStateOf("") }
    var mostrarDialogoNuevo by remember { mutableStateOf(false) }
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevoAutor by remember { mutableStateOf("") }

    // ESTADOS DEL DESPLEGABLE
    var expandido by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf(Categoria.listaCategorias[0]) }

    // LÓGICA DE FILTRADO (TÍTULO O AUTOR)
    val librosFiltrados = remember(estado, libros) {
        if (estado.isBlank()) libros
        else {
            libros.filter {
                it.titulo.contains(estado, ignoreCase = true) ||
                        it.autor.contains(estado, ignoreCase = true)
            }
        }
    }

    // --- DIÁLOGO PARA AÑADIR LIBRO NUEVO ---
    if (mostrarDialogoNuevo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevo = false },
            title = { Text("Nuevo Libro", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Introduce los detalles del libro.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nuevoTitulo,
                        onValueChange = { nuevoTitulo = it },
                        label = { Text("Título del libro") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nuevoAutor,
                        onValueChange = { nuevoAutor = it },
                        label = { Text("Autor") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Categoría", style = MaterialTheme.typography.labelMedium)

                    // DESPLEGABLE DE CATEGORÍAS
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expandido,
                            onExpandedChange = { expandido = !expandido }
                        ) {
                            OutlinedTextField(
                                value = categoriaSeleccionada.nombre,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            ExposedDropdownMenu(
                                expanded = expandido,
                                onDismissRequest = { expandido = false }
                            ) {
                                Categoria.listaCategorias.forEach { categoria ->
                                    DropdownMenuItem(
                                        text = { Text(categoria.nombre) },
                                        onClick = {
                                            categoriaSeleccionada = categoria
                                            expandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nuevoTitulo.isNotBlank()) {
                            libroViewModel.agregarLibro(nuevoTitulo, nuevoAutor, "", categoriaSeleccionada)
                            mostrarDialogoNuevo = false
                            nuevoTitulo = ""
                            nuevoAutor = ""
                        }
                    }
                ) { Text("Añadir") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNuevo = false }) { Text("Cancelar") }
            }
        )
    }

    // --- DISEÑO PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Buscar Libro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = estado,
            onValueChange = { estado = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Busca el título de tu libro") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Lista central (ocupa el espacio sobrante)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(librosFiltrados) { libro ->
                LibroFiltradoItem(libro) {
                    // Acción al seleccionar un libro de la lista
                    println("Seleccionado: ${libro.titulo}")
                }
            }
        }

        // --- SECCIÓN INFERIOR ---
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "¿No encuentras tu libro?", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { mostrarDialogoNuevo = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("Añadir libro")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Guardar (Finaliza la actividad)
        Button(
            onClick = { (context as? android.app.Activity)?.finish() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LibroFiltradoItem(libro: Libro, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        color = Color.Transparent
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${libro.titulo} - ${libro.autor}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = libro.categoria.nombre,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }
            Divider(modifier = Modifier.padding(top = 12.dp), thickness = 0.5.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLibroScreen() {
    BookScreen()
}