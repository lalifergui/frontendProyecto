package com.example.pantallas.ui.editarPerfil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pantallas.ui.fotoUsuario.FotoUsuario
import com.example.pantallas.ui.perfil.Perfil // 🎯 Importamos la pantalla de Perfil

class EditarPerfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EditarPerfilVentana()
        }
    }
}

@Composable
fun EditarPerfilVentana(viewModel: EditarPerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)

    val sharedPref = remember { context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE) }
    val miIdSesion = sharedPref.getLong("ID_USUARIO_ACTUAL", -1L)

    val usuarioIdReal = remember {
        val idFromIntent = activity?.intent?.getLongExtra("USUARIO_ID", -1L) ?: -1L
        if (idFromIntent != -1L) idFromIntent else miIdSesion
    }

    // Esta variable detecta si venimos de "Mi Perfil" (true) o del Registro (false)
    val esEdicion = remember { activity?.intent?.getBooleanExtra("Edicion", false) ?: false }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.fotoPerfil = it.toString()
        }
    }

    LaunchedEffect(usuarioIdReal) {
        if (usuarioIdReal != -1L) {
            viewModel.cargarDatosParaEditar(usuarioId = usuarioIdReal)
        } else {
            viewModel.limpiarCampos()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Editar Perfil", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        // CÍRCULO DE FOTO
        Box(
            modifier = Modifier.size(120.dp).padding(8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.fotoPerfil.isNotEmpty()) {
                    AsyncImage(
                        model = viewModel.fotoPerfil,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                }
            }
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        PantallaConTextEditable(valor = viewModel.nombre, onTextFieldChange = { viewModel.nombre = it }, etiqueta = "Nombre")
        PantallaConTextEditable(valor = viewModel.apellidos, onTextFieldChange = { viewModel.apellidos = it }, etiqueta = "Apellidos")
        PantallaConTextEditable(
            valor = viewModel.fechaNacimiento,
            onTextFieldChange = { viewModel.fechaNacimiento = it },
            etiqueta = "Fecha De Nacimiento (AAAA-MM-DD)"
        )
        PantallaConTextEditable(valor = viewModel.ciudad, onTextFieldChange = { viewModel.ciudad = it }, etiqueta = "Ciudad")

        Spacer(modifier = Modifier.height(40.dp))

        // BOTÓN GUARDAR CON LÓGICA DE NAVEGACIÓN CORREGIDA
        Button(
            onClick = {
                viewModel.actualizarPerfil(usuarioId = usuarioIdReal) {
                    if (esEdicion) {
                        // SI ES EDICIÓN: Vamos directo a la carta de presentación (Perfil)
                        val intent = Intent(context, Perfil::class.java).apply {
                            putExtra("USUARIO_ID", usuarioIdReal)
                            // Evitamos que el usuario vuelva a este formulario al dar "atrás"
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        context.startActivity(intent)
                        activity?.finish()
                    } else {
                        // SI ES REGISTRO: Seguimos el flujo normal (Foto -> Biblioteca)
                        val intent = Intent(context, FotoUsuario::class.java).apply {
                            putExtra("USUARIO_ID", usuarioIdReal)
                            putExtra("nombre", viewModel.nombre)
                            putExtra("apellidos", viewModel.apellidos)
                            putExtra("ciudad", viewModel.ciudad)
                            putExtra("fecha", viewModel.fechaNacimiento)
                            putExtra("foto", viewModel.fotoPerfil)
                        }
                        context.startActivity(intent)
                    }
                }
            },
            enabled = viewModel.botonHabilitado && !viewModel.estaCargando,
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (viewModel.estaCargando) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Guardar Cambios")
            }
        }
    }
}

@Composable
fun PantallaConTextEditable(valor: String, onTextFieldChange: (String) -> Unit, etiqueta: String) {
    OutlinedTextField(
        value = valor,
        onValueChange = onTextFieldChange,
        label = { Text(etiqueta) },
        modifier = Modifier.fillMaxWidth(0.9f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
}