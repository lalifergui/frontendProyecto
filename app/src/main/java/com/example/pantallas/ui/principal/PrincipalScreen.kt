package com.example.pantallas.ui.principal

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

@Composable
fun PrincipalScreen(
    principalViewModel: PrincipalViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp).verticalScroll(rememberScrollState()).statusBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("BIBLIO-SWIPE", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))

        // CategoriaDropdown aquí...

        Column(Modifier.fillMaxWidth().border(2.dp, Color.Gray, RoundedCornerShape(8.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CardPerfil(perfil = perfilViewModel.perfil)
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth()) {
                BibliotecaContenido(esModoEdicion = false) // 🎯 Sin el "+"
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Icon(painterResource(R.drawable.libro_x), null, Modifier.size(50.dp).clickable { principalViewModel.dislikeLibro() }, tint = Color.Unspecified)
                Icon(painterResource(R.drawable.libro_tick), null, Modifier.size(50.dp).clickable { /* Like */ }, tint = Color.Unspecified)
            }
        }
        Menu(context)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() { PrincipalScreen() }