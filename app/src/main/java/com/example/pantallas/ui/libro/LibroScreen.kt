package com.example.pantallas.ui.libro

import android.app.Activity
import android.content.Intent
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
    val activity = context as? Activity

    // Obtenemos la lista de libros del ViewModel
    val libros by libroViewModel.libros.collectAsState()

    // --- ESTADOS ---
    var estadoBusqueda by remember { mutableStateOf("") }

    // Estados para el Diálogo de Nuevo Libro
    var mostrarDialogoNuevo by remember { mutableStateOf(false) }
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevoAutor by remember { mutableStateOf("") }

    // Estados para el Desplegable de Categoría
    var expandido by remember { mutableStateOf(false) }
    // Importante: Asegúrate de que tus categorías tengan ID (1L, 2L...)
    var categoriaSeleccionada by remember {
        mutableStateOf(Categoria.listaCategorias.firstOrNull() ?: Categoria(1L, "General"))
    }

    // Estado de carga para evitar pulsar el botón dos veces
    var guardandoLibro by remember { mutableStateOf(false) }

    // --- LÓGICA DE FILTRADO ---
    val librosFiltrados = remember(estadoBusqueda, libros) {
        if (estadoBusqueda.isBlank()) libros
        else {
            libros.filter {
                it.titulo.contains(estadoBusqueda, ignoreCase = true) ||
                        it.autor.contains(estadoBusqueda, ignoreCase = true)
            }
        }
    }

    // --- FUNCIÓN CLAVE: DEVOLVER LIBRO A LA BIBLIOTECA ---
    fun devolverLibroSeleccionado(libro: Libro) {
        val intent = Intent().apply {
            // Pasamos los datos del libro pieza por pieza
            putExtra("LIBRO_ID", libro.id)
            putExtra("LIBRO_TITULO", libro.titulo)
            putExtra("LIBRO_AUTOR", libro.autor)
            putExtra("CATEGORIA_NOMBRE", libro.categoria.nombre)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    // --- DIÁLOGO NUEVO LIBRO ---
    if (mostrarDialogoNuevo) {
        AlertDialog(
            onDismissRequest = { if (!guardandoLibro) mostrarDialogoNuevo = false },
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

                    // Desplegable Categoría
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
                    enabled = !guardandoLibro,
                    onClick = {
                        if (nuevoTitulo.isNotBlank()) {
                            guardandoLibro = true

                            // LLAMADA AL BACKEND A TRAVÉS DEL VIEWMODEL
                            libroViewModel.crearLibroManualmente(
                                titulo = nuevoTitulo,
                                autor = nuevoAutor,
                                categoria = categoriaSeleccionada
                            ) { libroCreado ->
                                guardandoLibro = false
                                if (libroCreado != null) {
                                    // ÉXITO: El libro ya existe en BBDD con ID real
                                    devolverLibroSeleccionado(libroCreado)
                                    mostrarDialogoNuevo = false
                                } else {
                                    // ERROR: El libro no se pudo guardar
                                    println("Error al guardar libro en servidor")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
                ) {
                    if(guardandoLibro) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Añadir y Seleccionar")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoNuevo = false },
                    enabled = !guardandoLibro
                ) { Text("Cancelar") }
            }
        )
    }

    // --- UI PRINCIPAL ---
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

        // Barra de Búsqueda
        OutlinedTextField(
            value = estadoBusqueda,
            onValueChange = { estadoBusqueda = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Busca el título de tu libro") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Lista de Resultados
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(librosFiltrados) { libro ->
                LibroFiltradoItem(libro) {
                    // AL HACER CLIC EN UN LIBRO DE LA LISTA
                    devolverLibroSeleccionado(libro)
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Sección Inferior: Crear nuevo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "¿No encuentras tu libro?", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { mostrarDialogoNuevo = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("Añadir libro manualmente")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Cancelar/Volver
        Button(
            onClick = { activity?.finish() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LibroFiltradoItem(libro: Libro, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        color = Color.Transparent
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = libro.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${libro.autor} • ${libro.categoria.nombre}",
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