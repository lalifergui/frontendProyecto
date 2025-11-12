package com.example.pantallas.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.ui.registro.Registrar
import com.example.pantallas.R


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaLogin(
                //para que funcione el texto de reggit commitistrate ahora
                onRegisterClick = {
                    val intento = Intent(this, Registrar::class.java)
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
    onRegisterClick: () -> Unit = {}
) {
    LoginScreen(viewModel, onLoginClick, onForgotPasswordClick, onRegisterClick)
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    // Estado de los campos de texto
    val login by viewModel.login.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val botonHabilitado by viewModel.botonHabilitado.collectAsState(initial = false)
    val errorUsuario by viewModel.errorUsuario.collectAsState()
    val errorPassword by viewModel.errorPassword.collectAsState()
    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()), // permite scroll si la pantalla es pequeña
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Image(
            painter = painterResource(id= R.drawable.biblio),
            contentDescription = "Logo biblioswipe",
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Log in",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        MiTextField(
            value = login.usuario,
            onValueChange = { nuevoEmail -> viewModel.actualizarLogin(login.copy(usuario = nuevoEmail)) },
            label = "Email",

            isError = errorUsuario,
            leadingIcon = {Icon(Icons.Default.Email, contentDescription = "Icono Email")},
            errorMessage= "Usuario inválido, ingrese un email correcto."
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = login.password,
            onValueChange = { nuevoPassword -> viewModel.actualizarLogin(login.copy(password = nuevoPassword))},
            label = { Text("Contraseña") },
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image=if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }

                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorPassword, // usamos el estado de vm
            leadingIcon = {Icon(Icons.Default.Password, contentDescription = "Icono Contraseña")},
            singleLine = true,
            supportingText = {
                if(errorPassword){
                    Text(
                        text = "Contraseña inválida.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            enabled = botonHabilitado,
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

        Spacer(modifier = Modifier.height(20.dp))

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
        Spacer(modifier = Modifier.height(50.dp))
    }
}
@Composable
fun MiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: (@Composable (() -> Unit))? = null,

    ) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        isError=isError,
        leadingIcon=leadingIcon,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

// Preview debe estar fuera de toda clase o función
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    PantallaLogin()
}
