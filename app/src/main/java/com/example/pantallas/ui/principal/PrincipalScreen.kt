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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.R
import com.example.pantallas.modelos.Categoria
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.perfil.PerfilViewModel
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
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current

    // ---------------------------------------------------------
    // 1. RECUPERAR ID DE MEMORIA
    // ---------------------------------------------------------
    val usuarioIdLogueado = remember {
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            .getLong("ID_USUARIO_ACTUAL", -1L)
    }

    var expandido by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf(Categoria.listaCategorias[0]) }

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
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
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

            // Card Principal
            Column(
                Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardPerfil(perfil = perfilViewModel.perfil)
                Spacer(Modifier.height(12.dp))
                Box(Modifier.fillMaxWidth()) {
                    BibliotecaContenido(esModoEdicion = false)
                }
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Icon(
                        painterResource(R.drawable.libro_x), // Asegúrate de tener este icono
                        null,
                        Modifier.size(30.dp).clickable { principalViewModel.dislikeUsuario() },
                        tint = Color.Unspecified
                    )
                    Icon(
                        painterResource(R.drawable.libro_tick), // Asegúrate de tener este icono
                        null,
                        Modifier.size(30.dp).clickable { /* Lógica Like */ },
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ---------------------------------------------------------
            // 2. PASAR ID AL MENÚ (Esto arregla la navegación al perfil)
            // ---------------------------------------------------------
            Menu(context, usuarioIdLogueado)

            Spacer(Modifier.height(20.dp))
        }
    }
}