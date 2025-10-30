package com.example.pantallas.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.registro.Registrar



class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           PantallaLogin(
               //para que funcione el texto de reggit commitistrate ahora
               onRegisterClick = {
                   val intento= Intent(this, Registrar::class.java)
                   startActivity(intento)
               }
           )
        }
    }
}
@Composable
fun PantallaLogin(
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}){
    LoginScreen(viewModel,onLoginClick,onForgotPasswordClick,onRegisterClick)
}
@Composable
fun LoginScreen(
    viewModel: LoginViewModel=viewModel(),
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    // Estado de los campos de texto
    val usuario by viewModel.usuario.collectAsState()
    val password by viewModel.password.collectAsState()

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()), // permite scroll si la pantalla es pequeña
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Log in",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {viewModel.actualizarUsuario(it)},
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.actualizarPassword(it)},
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Log in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color(0xFF2196F3),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onForgotPasswordClick() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "¿No eres miembro?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Regístrate ahora",
            color = Color(0xFF2196F3),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}

// Preview debe estar fuera de toda clase o función
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    PantallaLogin()
}
