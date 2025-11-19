package com.example.pantallas.ui.perfil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.editarBiblioteca.EditarBiblioteca
import com.example.pantallas.ui.editarPerfil.EditarPerfil
import com.example.pantallas.util.CardPerfil // Asegúrate de que esta importación sea correcta
import com.example.pantallas.util.Menu

class Perfil : ComponentActivity() {
    // ... (onCreate se mantiene igual) ...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerfilAnyadir()
        }
    }
}

// Nota: La función CampoDeDato se mantiene, pero no se usará en PerfilAnyadir.

// =================================================================
// COMPOSABLE PRINCIPAL DE LA PANTALLA DE PERFIL
// =================================================================
@Composable
fun PerfilAnyadir(
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val perfilData = perfilViewModel.perfil

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // --- Título y Botones de Acción ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Perfil",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            // Íconos de Acción
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                // ICONO 1: Editar Perfil (Lápiz)
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar Perfil",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            val intent = Intent(context, EditarPerfil::class.java)
                            context.startActivity(intent)
                        }
                )


            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- INICIO: Card Perfil ---
        // Se reemplaza el bloque de Column y CampoDeDato por el CardPerfil
        CardPerfil(perfil = perfilData)
        // --- FIN: Card Perfil ---


        // --- INICIO: Hueco para la futura Biblioteca ---
        Spacer(modifier = Modifier.height(32.dp))
        // ICONO 2: Editar Biblioteca
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
        }
        Text(
            text = "Zona de la Biblioteca (Próximamente)",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // Este gran Spacer dejará el espacio entre el Card y el Menu inferior
        Spacer(modifier = Modifier.height(300.dp))
        // --- FIN: Hueco para la futura Biblioteca ---

        // El Menu se mantiene dentro del Column para que el scroll funcione con él
        Menu(context)
    }
}

// ... (La Preview se mantiene igual) ...
// =================================================================
// 3. PREVIEW
// =================================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPerfilScreen() {
    PerfilAnyadir()
}