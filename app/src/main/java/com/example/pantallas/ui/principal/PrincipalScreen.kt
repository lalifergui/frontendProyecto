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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.pantallas.ui.biblioteca.BibliotecaContenido
import com.example.pantallas.ui.biblioteca.BibliotecaViewModel // Importación lógica
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
    //INTEGRACIÓN DE LOS TRES VIEWMODELS
    principalViewModel: PrincipalViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel(),
    bibliotecaViewModel: BibliotecaViewModel = viewModel()
) {
    // OBTENCIÓN DE DATOS PARA LA LÓGICA Y VISUAL
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

        Spacer(modifier = Modifier.height(12.dp))

        // --- Column con borde gris para todo el contenido de swipe ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card del perfil
            CardPerfil(perfil = perfilData)

            Spacer(modifier = Modifier.height(12.dp))

            // Contenido de la biblioteca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Asumimos que BibliotecaContenido usa @Composable y no necesita pasar el VM directamente
                BibliotecaContenido()
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Iconos de acción
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
                            principalViewModel.dislikeLibro() // 🎯 LÓGICA CONECTADA
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
                            // 🎯 LÓGICA CONECTADA
                            libroActual?.let {
                                principalViewModel.likeLibro(it, usuarioTargetId)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Menú fuera del borde
        Menu(context)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() {
    PrincipalScreen()
}