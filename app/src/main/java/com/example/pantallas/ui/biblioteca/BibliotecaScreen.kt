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

// ===========================================================
// ACTIVITY
// ===========================================================
class Biblioteca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaScreen()
        }
    }
}

// ===========================================================
// COMPONENTE: Tarjeta de libro
// ===========================================================
@Composable
fun LibroCard(libro: Libro) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .height(105.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = libro.titulo,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = libro.categoria.nombre,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}

// ===========================================================
// COMPONENTE: Sección de libros
// ===========================================================
@Composable
fun SeccionLibros(titulo: String, libros: List<Libro>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 10.dp)
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

// ===========================================================
// COMPONENTE: Contenido reutilizable de la Biblioteca
// ===========================================================
@Composable
fun BibliotecaContenido(viewModel: BibliotecaViewModel = viewModel()) {

    val bibliotecaData = viewModel.biblioteca
    val ColorExteriorEstanteria = Color(0xFFF5E7D3)
    val ColorBordeEstanteria = Color.Gray.copy(alpha = 0.5f)
    val ColorLineaEstanteria = Color.Gray.copy(alpha = 0.5f)
    val GrosorBorde = 2.dp
    val GrosorLinea = 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorExteriorEstanteria)
            .border(GrosorBorde, ColorBordeEstanteria, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            SeccionLibros("Recomendados", bibliotecaData.librosRecomendados)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GrosorLinea)
                    .background(ColorLineaEstanteria)
            )

            SeccionLibros("Últimos libros", bibliotecaData.librosLeidos)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GrosorLinea)
                    .background(ColorLineaEstanteria)
            )

            SeccionLibros("Futuras lecturas", bibliotecaData.librosFuturasLecturas)
        }
    }
}

// ===========================================================
// PANTALLA COMPLETA
// ===========================================================
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
            text = "Mi Biblioteca",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            BibliotecaContenido(viewModel = viewModel)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// ===========================================================
// PREVIEW
// ===========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBibliotecaScreen() {
    BibliotecaScreen()
}
