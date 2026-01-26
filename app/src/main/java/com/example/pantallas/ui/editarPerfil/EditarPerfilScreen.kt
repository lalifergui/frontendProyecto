package com.example.pantallas.ui.editarPerfil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.fotoUsuario.FotoUsuario
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

class EditarPerfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EditarPerfilVentana()
        }
    }
}@Composable
fun EditarPerfilVentana(viewModel: EditarPerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = (context as? Activity)

    // ID Dinámico para no sobreescribir usuarios en MySQL
    val usuarioIdReal = remember { activity?.intent?.getLongExtra("USUARIO_ID", 1L) ?: 1L }
    val esEdicion = remember { activity?.intent?.getBooleanExtra("Edicion", false) ?: false }

    // Launcher para la Galería
    var fotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    LaunchedEffect(usuarioIdReal) {
        if (esEdicion &&usuarioIdReal!=-1L) {
            viewModel.cargarDatosParaEditar(usuarioId = usuarioIdReal)
        } else {
            viewModel.limpiarCampos()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
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
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = null,
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

        // CAMPOS DE TEXTO vinculados al ViewModel
        PantallaConTextEditable(valor = viewModel.nombre, onTextFieldChange = { viewModel.nombre = it }, etiqueta = "Nombre")
        PantallaConTextEditable(valor = viewModel.apellidos, onTextFieldChange = { viewModel.apellidos = it }, etiqueta = "Apellidos")
        PantallaConTextEditable(valor = viewModel.fechaNacimiento, onTextFieldChange = { viewModel.fechaNacimiento = it }, etiqueta = "Fecha De Nacimiento (AAAA-MM-DD)")
        PantallaConTextEditable(valor = viewModel.ciudad, onTextFieldChange = { viewModel.ciudad = it }, etiqueta = "Ciudad")

        Spacer(modifier = Modifier.height(40.dp))

        // 🎯 BOTÓN CORREGIDO
        Button(
            onClick = {
                viewModel.actualizarPerfil(usuarioId = usuarioIdReal) {
                    // 🎯 SOLUCIÓN: Usamos las propiedades directas del viewModel
                    val intent = Intent(context, FotoUsuario::class.java).apply {
                        putExtra("USUARIO_ID", usuarioIdReal)
                        putExtra("nombre", viewModel.nombre)
                        putExtra("apellidos", viewModel.apellidos)
                        putExtra("ciudad", viewModel.ciudad)
                        putExtra("fecha", viewModel.fechaNacimiento)
                    }
                    context.startActivity(intent)
                    if (esEdicion) activity?.finish() // Si era edición, cerramos la ventana al terminar
                }
            },
            enabled = viewModel.botonHabilitado && !viewModel.estaCargando, // No permitir clics dobles mientras carga
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
fun PantallaConTextEditable(valor: String, onTextFieldChange: (String) -> Unit, etiqueta: String, leadingIcon: @Composable (() -> Unit)? = null) {
    OutlinedTextField(value = valor, onValueChange = onTextFieldChange, label = { Text(etiqueta) }, leadingIcon = leadingIcon, modifier = Modifier.fillMaxWidth(0.9f), singleLine = true, shape = RoundedCornerShape(12.dp))
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEditarPerfil() {
    EditarPerfilVentana()
}