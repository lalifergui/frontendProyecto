package com.example.pantallas.ui.libro


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.modelos.Libro


class LibroScreen  : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookScreen()
        }
    }
}

@Composable
fun BookScreen(
    viewModel: LibroViewModel = viewModel()
){
    PantallaLibros(viewModel)
}
@Composable
fun PantallaLibros(libroViewModel: LibroViewModel) {
    val libros by libroViewModel.libros.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Lista de Libros", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(libros) { libro ->
                LibroItem(libro)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Ejemplo: agregar libro de prueba
            val categoriaEjemplo = Categoria(1, "Ficción")
            libroViewModel.agregarLibro(
                titulo = "Nuevo Libro",
                autor = "Autor",
                portada = "",
                categoria = categoriaEjemplo
            )
        }) {
            Text("Agregar Libro de prueba")
        }
    }
}

@Composable
fun LibroItem(libro: Libro) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text("Título: ${libro.titulo}")
        Text("Autor: ${libro.autor}")
        Text("Categoría: ${libro.categoria.nombre}")
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    BookScreen()
}