package com.example.pantallas.ui.biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.Libro
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaScreen()
        }
    }
}

// =================================================================
// Componente para la Tarjeta de un Solo Libro (LibroCard) - Se mantiene igual
// =================================================================
@Composable
fun LibroCard(libro: Libro) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder de la Portada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .padding(4.dp), // Añadimos padding interno para que el texto no se pegue a los bordes
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = libro.titulo,
                fontSize = 12.sp, // CLAVE: Reducimos ligeramente el tamaño de fuente (de 14sp a 12sp)
                textAlign = TextAlign.Center,
                lineHeight = 14.sp, // Aseguramos un interlineado adecuado
                maxLines = 4, // CLAVE: Permitimos hasta 4 líneas
                overflow = TextOverflow.Ellipsis // Mantenemos elipsis por si acaso
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Categoría (el texto de abajo)
        Text(
            text = libro.categoria.nombre,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}

// =================================================================
// Componente para una Sección Completa (Se adapta a estar dentro de un gran Box)
// =================================================================
@Composable
fun SeccionLibros(titulo: String, libros: List<Libro>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp) // Espacio vertical dentro de la sección
            .padding(horizontal = 12.dp) // Margen de los lados para que los libros no choquen con el borde grande
    ) {
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color.Black // El texto es negro sobre el fondo blanco del interior de la estantería
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            libros.forEach { libro ->
                LibroCard(libro = libro)
            }
        }
    }
}

// =================================================================
// PANTALLA PRINCIPAL DE LA BIBLIOTECA (Contenedor Único con Líneas Internas)
// =================================================================
@Composable
fun BibliotecaScreen(
    viewModel: BibliotecaViewModel = viewModel()
) {
    val bibliotecaData = viewModel.biblioteca
    val ColorExteriorEstanteria = Color(0xFFF5E7D3)
    val ColorBordeEstanteria = Color.Gray.copy(alpha = 0.5f) // Borde del recuadro grande
    val ColorLineaEstanteria = Color.Gray.copy(alpha = 0.5f) // Línea entre secciones
    val GrosorBorde = 2.dp
    val GrosorLinea = 1.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo general de la pantalla
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(), // Para manejar las barras del sistema
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Biblioteca",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        // --- CONTENEDOR GRANDE DE LA ESTANTERÍA ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Margen de la estantería respecto a los lados de la pantalla
                .background(ColorExteriorEstanteria) // Fondo ligeramente gris para el interior de la estantería
                .border(GrosorBorde, ColorBordeEstanteria, RoundedCornerShape(8.dp)) // Borde del recuadro grande
                .clip(RoundedCornerShape(8.dp)) // Asegura que el contenido respete las esquinas redondeadas
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Libros Recomendados
                SeccionLibros(
                    titulo = "Recomendados",
                    libros = bibliotecaData.librosRecomendados
                )

                // 2. Línea separadora (Estantería)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(GrosorLinea)
                        .background(ColorLineaEstanteria)
                )

                // 3. Últimos Libros Leídos
                SeccionLibros(
                    titulo = "Últimos libros",
                    libros = bibliotecaData.librosLeidos
                )

                // 4. Línea separadora (Estantería)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(GrosorLinea)
                        .background(ColorLineaEstanteria)
                )

                // 5. Futuras Lecturas
                SeccionLibros(
                    titulo = "Futuras lecturas",
                    libros = bibliotecaData.librosFuturasLecturas
                )
            }
        }
        // --- FIN DEL CONTENEDOR GRANDE ---

        Spacer(modifier = Modifier.height(30.dp)) // Espacio final en el fondo blanco de la pantalla
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBibliotecaScreen() {
    BibliotecaScreen()
}