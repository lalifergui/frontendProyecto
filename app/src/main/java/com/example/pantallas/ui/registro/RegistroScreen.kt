package com.example.pantallas.ui.registro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.pantallas.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.editarPerfil.EditarPerfil

class Registrar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaPrincipal()
        }
    }
}

@Composable
fun PantallaPrincipal(viewModel: RegistroViewModel = viewModel()) {
    RegistroScreen(viewModel)
}

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistrarClick: () -> Unit = {}
) {
    val usuario by viewModel.usuario.collectAsState()
    val errorEmail by viewModel.errorEmail.collectAsState()
    val errorPassword by viewModel.errorPassword.collectAsState()
    val botonHabilitado by viewModel.botonHabilitado.collectAsState(initial = false)

    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // --- DIÁLOGO EMERGENTE CON NAVEGACIÓN CONFIGURADA ---
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Registro completado", fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Desea seguir rellenando su perfil con las categorías y las fotos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        //SI: Navega a EditarPerfil
                        val intent = Intent(context, EditarPerfil::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text("SI", color = Color(0xFF4285F4), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        // NO: Navega a Perfil
                        val intent = Intent(context, Perfil::class.java)
                        intent.putExtra("NUEVO_REGISTRO", true)
                        context.startActivity(intent)
                    }
                ) {
                    Text("NO", color = Color.Gray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.biblio),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Crear Usuario", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // Bloque de campos de entrada
        MiTextField(
            value = usuario.usuario,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(usuario = it)) },
            label = "Nombre",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MiTextField(
            value = usuario.apellidos,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(apellidos = it)) },
            label = "Apellidos",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MiTextField(
            value = usuario.fechaNacimiento,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(fechaNacimiento = it)) },
            label = "Fecha Nacimiento",
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MiTextField(
            value = usuario.ciudad,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(ciudad = it)) },
            label = "Ciudad",
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MiTextField(
            value = usuario.email,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(email = it)) },
            label = "Email",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        if (errorEmail) {
            Text(
                text = "Email incorrecto",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = usuario.password,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(password = it)) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorPassword,
            leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) },
            singleLine = true
        )
        if (errorPassword) {
            Text(
                text = "Mínimo 4 caracteres",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón habilitado dinámicamente según el ViewModel
        Button(
            onClick = { mostrarDialogo = true },
            enabled = botonHabilitado,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registrar")
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun MiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: (@Composable (() -> Unit))? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreen() {
    PantallaPrincipal()
}