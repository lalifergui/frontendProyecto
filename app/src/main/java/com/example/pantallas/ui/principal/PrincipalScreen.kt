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
    // Usamos un ViewModel de biblioteca independiente para los perfiles sugeridos
    bibliotecaSugeridoViewModel: BibliotecaViewModel = viewModel()
) {
    val context = LocalContext.current

    // 1. RECUPERAR ID DEL USUARIO LOGUEADO (Laura)
    val usuarioIdLogueado = remember {
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            .getLong("ID_USUARIO_ACTUAL", -1L)
    }

    // 2. DISPARAR CARGA DE EXPLORACIÓN AL INICIAR
    LaunchedEffect(usuarioIdLogueado) {
        if (usuarioIdLogueado != -1L) {
            principalViewModel.cargarExploracion(usuarioIdLogueado)
        }
    }

    val sugerido = principalViewModel.usuarioSugerido
    LaunchedEffect(sugerido) {
        sugerido?.let {
            // Le pedimos al ViewModel de biblioteca que traiga los libros del ID sugerido
            bibliotecaSugeridoViewModel.cargarBibliotecaReal(it.perfil.perfil_id)
        }
    }
    var expandido by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf(Categoria.listaCategorias[0]) }

    // 3. ACTUALIZAR BIBLIOTECA DEL USUARIO QUE APARECE EN EL SWIPE
    LaunchedEffect(sugerido) {
        sugerido?.let {
            bibliotecaSugeridoViewModel.cargarBibliotecaReal(it.perfil.perfil_id)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "BIBLIO-SWIPE",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Desplegable de Categorías
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
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
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

            // --- CONTENIDO DINÁMICO ---
            if (principalViewModel.isLoading) {
                // Muestra el círculo de carga mientras se conecta al backend
                Box(Modifier.height(400.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2196F3))
                }
            } else if (sugerido != null) {
                // TARJETA DEL OTRO USUARIO (Emi, Sandra, Adriel...)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mapea los datos reales del perfil sugerido
                    CardPerfil(perfil = sugerido.perfil)

                    Spacer(Modifier.height(12.dp))

                    if (bibliotecaSugeridoViewModel.tieneLibros) {
                        // Si tiene libros en MySQL, los mostramos
                        BibliotecaContenido(
                            viewModel = bibliotecaSugeridoViewModel,
                            esModoEdicion = false
                        )
                    } else {
                        // Si no tiene nada (como Adriel en algunas tablas), avisamos
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
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

                    // BOTONES DE ACCIÓN (X y TICK)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        // Icono X: Llama a descartar (pasa al siguiente ID)
                        Icon(
                            painter = painterResource(id = R.drawable.libro_x),
                            contentDescription = "Descartar",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { principalViewModel.descartar() },
                            tint = Color.Unspecified
                        )
                        // Icono Tick: Guarda favorito en la DB y pasa al siguiente
                        Icon(
                            painter = painterResource(id = R.drawable.libro_tick),
                            contentDescription = "Like",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    principalViewModel.darLike(
                                        usuarioIdLogueado,
                                        sugerido.perfil.perfil_id
                                    )
                                },
                            tint = Color.Unspecified
                        )
                    }
                }
            } else {
                // Estado final cuando se agotan los perfiles en la base de datos
                Box(Modifier.height(400.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay más lectores disponibles.\n¡Prueba otra categoría!",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Menu(context, usuarioIdLogueado)
            Spacer(Modifier.height(20.dp))
        }
    }
}