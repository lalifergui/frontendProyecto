package com.example.pantallas.ui.fotoUsuario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.registro.PantallaPrincipal
import com.example.pantallas.util.CardPerfil

class FotoUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FotoUsuarioScreen()
        }
    }
}

@Composable
fun FotoUsuarioScreen(viewModel: FotoUsuarioViewModel = viewModel()) {
    val context = LocalContext.current
    val perfil by viewModel.perfil.collectAsState()
    val fotoUri by viewModel.fotoUri.collectAsState()

    // Launcher para seleccionar imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.actualizarFoto(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tu Carta de Presentación",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Así es como te verán otros usuarios",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Contenedor para superponer el botón "+" sobre la CardPerfil
        Box(contentAlignment = Alignment.CenterStart) {
            CardPerfil(perfil = perfil)

            // Superponemos el botón de añadir foto exactamente sobre el área del círculo de la Card
            Box(
                modifier = Modifier
                    .padding(start = 16.dp) // Alineado con el círculo de CardPerfil
                    .size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                // Si hay foto, la mostramos
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Botón "+"
                SmallFloatingActionButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    contentColor = Color.White,
                    modifier = Modifier.size(32.dp).align(Alignment.BottomEnd)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir foto")
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                val intent = Intent(context, Perfil::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar y ver mi Perfil")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFoto() {
    FotoUsuarioScreen()
}