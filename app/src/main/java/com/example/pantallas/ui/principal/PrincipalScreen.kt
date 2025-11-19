package com.example.pantallas.ui.principal

import android.content.Intent
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
//import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.favoritos.Favoritos
import com.example.pantallas.util.Menu
import com.example.pantallas.R
import com.example.pantallas.ui.editarBiblioteca.EditarBiblioteca
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
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val perfilData = perfilViewModel.perfil
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO ---
        Text(
            text = "BIBLIO-SWIPE",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CARD PERFIL ---
        CardPerfil(perfil = perfilData)

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN / ICONO EDITAR BIBLIOTECA ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = "Editar Biblioteca",
                tint = Color.Black,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        val intent = Intent(context, EditarBiblioteca::class.java)
                        context.startActivity(intent)
                    }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Zona de la Biblioteca (Próximamente)",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(120.dp))

        // --- LIKE / DISLIKE ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.libro_x),
                contentDescription = "No me gusta",
                tint = Color.Unspecified,
                modifier = Modifier.size(80.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.libro_tick),
                contentDescription = "Me gusta",
                tint = Color.Unspecified,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- MENÚ INFERIOR ---
        Menu(context)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() {
    PrincipalScreen()
}
