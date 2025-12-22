package com.example.pantallas.ui.principal

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.util.Menu
import com.example.pantallas.R
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.biblioteca.BibliotecaViewModel
import com.example.pantallas.ui.perfil.PerfilViewModel
import com.example.pantallas.util.CardPerfil

class Principal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrincipalScreen()
        }
    }
}

@Composable
fun PrincipalScreen(
    principalViewModel: PrincipalViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel(),
    bibliotecaViewModel: BibliotecaViewModel = viewModel()
) {
    val perfilData = perfilViewModel.perfil
    val libroActual = principalViewModel.libroActual
    val usuarioTargetId = principalViewModel.bibliotecaActualId
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "BIBLIO-SWIPE",
            color = Color.Black,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --- DROPDOWN CONECTADO AL VIEWMODEL ---
        CategoriaDropdown(
            categorias = Categoria.listaCategorias,
            onCategoriaSelected = { categoria ->
                // 🎯 CONEXIÓN: Enviamos la categoría al ViewModel para filtrar
                principalViewModel.filtrarPorCategoria(categoria)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardPerfil(perfil = perfilData)

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                BibliotecaContenido()
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ICONO DISLIKE
                Icon(
                    painter = painterResource(id = R.drawable.libro_x),
                    contentDescription = "No me gusta",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            principalViewModel.dislikeLibro()
                        }
                )

                // ICONO LIKE
                Icon(
                    painter = painterResource(id = R.drawable.libro_tick),
                    contentDescription = "Me gusta",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            libroActual?.let {
                                principalViewModel.likeLibro(it, usuarioTargetId)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Menu(context)
    }
}

@Composable
fun CategoriaDropdown(
    categorias: List<Categoria>,
    onCategoriaSelected: (Categoria) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Texto dinámico para mostrar la selección actual
    var selectedText by remember { mutableStateOf("Selecciona una categoría") }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable { expanded = true },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = selectedText)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "abrir"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nombre) },
                    onClick = {
                        selectedText = categoria.nombre
                        expanded = false
                        onCategoriaSelected(categoria)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() {
    PrincipalScreen()
}