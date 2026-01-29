package com.example.pantallas.ui.principal

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.R
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.biblioteca.BibliotecaViewModel
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu

class Principal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PrincipalScreen() }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    principalViewModel: PrincipalViewModel = viewModel(),
    bibliotecaSugeridoViewModel: BibliotecaViewModel = viewModel()
) {
    val context = LocalContext.current

    // 1. RECUPERAR ID Y ESTADOS
    val usuarioIdLogueado = remember {
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            .getLong("ID_USUARIO_ACTUAL", -1L)
    }

    var expandido by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf(Categoria.listaCategorias[0]) }
    val sugerido = principalViewModel.usuarioSugerido

    // 2. DISPARADORES DE CARGA
    LaunchedEffect(usuarioIdLogueado) {
        if (usuarioIdLogueado != -1L) {
            principalViewModel.cargarExploracion(usuarioIdLogueado)
        }
    }

    LaunchedEffect(sugerido) {
        sugerido?.let {
            bibliotecaSugeridoViewModel.cargarBibliotecaReal(it.perfil.perfil_id)
        }
    }

    // 3. ESTRUCTURA DE CAPAS (Box para fijar el menú)
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // CAPA 1: CONTENIDO DESPLAZABLE (Scroll)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                // Margen inferior para que el menú no tape los botones de acción
                .padding(bottom = 90.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "BIBLIO-SWIPE",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Selector de Categoría
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido }
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionada.nombre,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text="Filtrar por categoría", fontSize = 12.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.menuAnchor().fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
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

            // CONTENIDO DE LA TARJETA
            if (principalViewModel.isLoading) {
                CircularProgressIndicator(Modifier.padding(top = 50.dp))
            } else if (sugerido != null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardPerfil(perfil = sugerido.perfil)

                    Spacer(Modifier.height(12.dp))

                    // Lógica de Biblioteca Vacía o con Libros
                    if (bibliotecaSugeridoViewModel.tieneLibros) {
                        BibliotecaContenido(
                            viewModel = bibliotecaSugeridoViewModel,
                            esModoEdicion = false
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Este usuario aún no tiene libros en su biblioteca.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Botones de Swipe (X y Corazón)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Icon(
                            painter = painterResource(id = R.drawable.libro_x),
                            contentDescription = "Descartar",
                            modifier = Modifier.size(45.dp).clickable { principalViewModel.descartar() },
                            tint = Color.Unspecified
                        )
                        // Dentro de la Row de botones en PrincipalScreen (image_3614f6)
                        Icon(
                            painter = painterResource(id = R.drawable.libro_tick), // Tu corazón verde
                            contentDescription = "Like",
                            modifier = Modifier
                                .size(45.dp)
                                .clickable {
                                    val sugerido = principalViewModel.usuarioSugerido
                                    if (sugerido != null && usuarioIdLogueado != -1L) {
                                        // 🎯 Llama a la función que persiste en MySQL
                                        principalViewModel.darLike(usuarioIdLogueado, sugerido.perfil.perfil_id)
                                    }
                                },
                            tint = Color.Unspecified
                        )
                    }
                }
            } else {
                Text(
                    "No hay más lectores disponibles.",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
        }

        // CAPA 2: MENÚ ESTÁTICO (Fijado al fondo del Box)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
        ) {
            Menu(context, usuarioIdLogueado)
        }
    }
}