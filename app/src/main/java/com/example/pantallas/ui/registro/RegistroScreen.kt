package com.example.pantallas.ui.registro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.R
import com.example.pantallas.ui.editarPerfil.EditarPerfil
import com.example.pantallas.ui.theme.AppTheme

class Registrar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistroScreen(viewModel())
                }
            }
        }
    }
}
@Composable
fun RegistroScreen(viewModel: RegistroViewModel) {
    val usuario by viewModel.usuario.collectAsState()
    val errorEmail by viewModel.errorEmail.collectAsState()
    val errorPassword by viewModel.errorPassword.collectAsState()
    //  Añadimos la observación por si validas nombres con tildes aquí
    val errorNombre by viewModel.errorNombre.collectAsState()
    val mensajeErrorServidor by viewModel.mensajeErrorServidor.collectAsState()
    val botonHabilitado by viewModel.botonHabilitado.collectAsState()
    val usuarioIdGenerado by viewModel.usuarioIdGenerado.collectAsState()
    val estaCargando by viewModel.estaCargando.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(usuarioIdGenerado) {
        usuarioIdGenerado?.let { id ->
            val intent = Intent(context, EditarPerfil::class.java).apply {
                putExtra("USUARIO_ID", id)
                putExtra("Edicion", false)
            }
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).verticalScroll(rememberScrollState()).statusBarsPadding().navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Crear Usuario", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // Campo Email
        MiTextField(
            value = usuario.email,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(email = it)) },
            label = "Email",
            isError = errorEmail, //  Añadido para que se ponga rojo
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        if (errorEmail) {
            Text("Email inválido", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = usuario.password,
            onValueChange = { viewModel.actualizarUsuario(usuario.copy(password = it)) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorPassword,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            supportingText = {
                if (errorPassword) {
                    //  Mensaje actualizado a la nueva seguridad del ViewModel
                    Text("Mínimo 8 caracteres, Mayús, Núm y '*'", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        //  MENSAJE DE ERROR DEL SERVIDOR (Usuario ya existe)
        if (mensajeErrorServidor != null) {
            Text(
                text = mensajeErrorServidor!!,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { viewModel.registrar() },
            // 🎯 El botón ahora también depende de !errorNombre (tildes)
            enabled = botonHabilitado && !estaCargando,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (estaCargando) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Registrar")
            }
        }
    }
}

@Composable
fun MiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false, //  Añadido soporte para error
    leadingIcon: (@Composable (() -> Unit))? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}