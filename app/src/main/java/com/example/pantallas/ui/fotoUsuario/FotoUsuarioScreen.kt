package com.example.pantallas.ui.fotoUsuario

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.util.CardPerfil

class FotoUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Extraemos los datos del Intent
        val usuarioId = intent.getLongExtra("USUARIO_ID", 1L)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellidos = intent.getStringExtra("apellidos") ?: ""
        val ciudad = intent.getStringExtra("ciudad") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""

        setContent {
            // 2. Obtenemos el ViewModel
            val viewModel: FotoUsuarioViewModel = viewModel()

            // 3. Pasamos los datos al ViewModel antes de mostrar la pantalla
            LaunchedEffect(Unit) {
                viewModel.setDatosPerfil(nombre, apellidos, ciudad, fecha)
            }

            FotoUsuarioScreen(viewModel)
        }
    }
}

@Composable
fun FotoUsuarioScreen(viewModel: FotoUsuarioViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)

    // 1. Declaración del dispatcher para que el botón "Volver" funcione
    val backDispatcher = androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val perfil by viewModel.perfil.collectAsState()
    val fotoUri by viewModel.fotoUri.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            text = "Esta es la previsualización de lo que verían los otros usuarios sobre tu perfil.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- REPRESENTACIÓN DE LA CARD ---
        Box(contentAlignment = Alignment.CenterStart) {
            CardPerfil(perfil = perfil)

            // Círculo de la foto (Solo vista, sin botón +)
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                // Si fotoUri es null, la CardPerfil mostrará su diseño por defecto
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // --- BOTONES: VOLVER ATRÁS Y GUARDAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {backDispatcher?.onBackPressed()},
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver a Editar")
            }

            Button(
                onClick = {
                    val intent = Intent(context, Biblioteca::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                modifier = Modifier.weight(1f)

            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFotoFinal() {
    FotoUsuarioScreen()
}