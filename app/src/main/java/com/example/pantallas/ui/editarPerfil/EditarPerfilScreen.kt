package com.example.pantallas.ui.editarPerfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage // Requiere implementación de Coil
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.fotoUsuario.FotoUsuario

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
fun EditarPerfilVentana(
    viewModel: EditarPerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    //val activity = (context as? Activity)

    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }

    // Estado para la URI de la foto seleccionada
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    val camposRellenados = nombre.isNotBlank() &&
            apellidos.isNotBlank() &&
            fechaNacimiento.isNotBlank() &&
            ciudad.isNotBlank()
    // Launcher para abrir la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Editar Perfil",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))


        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Círculo de la foto
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
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.Gray
                    )
                }
            }

            // Botón circular pequeño con el +
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir foto",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PantallaConTextEditable(
                valor = viewModel.nombre,
                onTextFieldChange = { viewModel.nombre = it },
                etiqueta = "Nombre",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            PantallaConTextEditable(
                valor = viewModel.apellidos,
                onTextFieldChange = { viewModel.apellidos = it },
                etiqueta = "Apellidos",
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) }
            )

            PantallaConTextEditable(
                valor = viewModel.fechaNacimiento,
                onTextFieldChange = { viewModel.fechaNacimiento = it },
                etiqueta = "Fecha de Nacimiento",
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
            )

            PantallaConTextEditable(
                valor = viewModel.ciudad,
                onTextFieldChange = { viewModel.ciudad = it },
                etiqueta = "Ciudad",
                leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.actualizarPerfil(usuarioId = 1L)
                        val intent = Intent(context, FotoUsuario::class.java)
                        // Pasamos los datos a la siguiente pantalla
                        intent.putExtra("nombre", viewModel.nombre)
                        intent.putExtra("apellidos", viewModel.apellidos)
                        intent.putExtra("ciudad", viewModel.ciudad)
                        intent.putExtra("fecha", viewModel.fechaNacimiento)

                        context.startActivity(intent)
                    },
                    //EL BOTÓN AHORA DEPENDE DEL VIEWMODEL
                    enabled = viewModel.botonHabilitado,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar Cambios", color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PantallaConTextEditable(
    valor: String,
    onTextFieldChange: (String) -> Unit,
    etiqueta: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = valor,
        onValueChange = onTextFieldChange,
        label = { Text(etiqueta) },
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(0.9f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEditarPerfil() {
    EditarPerfilVentana()
}