package com.example.pantallas.ui.fotoUsuario

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pantallas.ui.principal.Principal // IMPORTANTE: Ir a Principal, no Biblioteca
import com.example.pantallas.util.CardPerfil

class FotoUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuarioId = intent.getLongExtra("USUARIO_ID", -1L)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellidos = intent.getStringExtra("apellidos") ?: ""
        val ciudad = intent.getStringExtra("ciudad") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""

        setContent {
            val viewModel: FotoUsuarioViewModel = viewModel()
            remember {
                viewModel.setDatosPerfil(nombre, apellidos, ciudad, fecha)
                true
            }
            FotoUsuarioScreen(viewModel, usuarioId)
        }
    }
}

@Composable
fun FotoUsuarioScreen(viewModel: FotoUsuarioViewModel, usuarioId: Long) {
    val context = LocalContext.current
    val activity = (context as? Activity)
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
        Text("Tu Carta de Presentación", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Previsualización de tu perfil.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(40.dp))

        Box(contentAlignment = Alignment.CenterStart) {
            CardPerfil(perfil = perfil)
            Box(
                modifier = Modifier.padding(start = 16.dp).size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { backDispatcher?.onBackPressed() }, modifier = Modifier.weight(1f)) {
                Text("Volver")
            }

            Button(
                onClick = {
                    // --- CORRECCIÓN CLAVE: GUARDAR ID EN MEMORIA ---
                    val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putLong("ID_USUARIO_ACTUAL", usuarioId)
                        apply()
                    }

                    // Ir a la pantalla Principal
                    val intent = Intent(context, Principal::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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