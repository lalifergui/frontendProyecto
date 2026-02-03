package com.example.pantallas.ui.login

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.example.pantallas.ui.registro.Registrar
import com.example.pantallas.ui.principal.Principal
import com.example.pantallas.R
import com.example.pantallas.ui.theme.AppTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaLogin(
                        onRegisterClick = {
                            val intento = Intent(this, Registrar::class.java)
                            startActivity(intento)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaLogin(
    viewModel: LoginViewModel = viewModel(),
    onRegisterClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val loginResult by viewModel.loginResult.collectAsState()
    // 🎯 Capturamos el error de "Email o contraseña incorrectos"
    val errorLogin by viewModel.errorLogin.collectAsState()

    // Manejo de éxito en el login
    LaunchedEffect(loginResult) {
        loginResult?.let { usuario ->
            val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                // Guardamos el ID real del usuario
                putLong("ID_USUARIO_ACTUAL", usuario.id)
                apply()
            }

            val intent = Intent(context, Principal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }

    LoginScreen(
        viewModel = viewModel,
        onRegisterClick = onRegisterClick,
        errorMessageFromServer = errorLogin // Pasamos el error a la UI
    )
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    errorMessageFromServer: String? = null // 🎯 Nuevo parámetro para mostrar el error 401/404
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorEmail by viewModel.errorEmail.collectAsState()
    val errorPassword by viewModel.errorPassword.collectAsState()
    val botonHabilitado by viewModel.botonHabilitado.collectAsState(initial = false)

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo biblioswipe",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text(text = "LOGIN", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(40.dp))

        // Campo Email
        MiTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = "Email",
            isError = errorEmail,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Icono Email") },
            errorMessage = "Email inválido."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorPassword,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Icono Contraseña") },
            singleLine = true,
            supportingText = {
                if (errorPassword) {
                    // 🎯 Mensaje actualizado a la nueva seguridad
                    Text(text = "8+ caracteres, Mayús y '*'.", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🎯 MOSTRAR ERROR DE CREDENCIALES (Si el email/pass no existen en la BD)
        if (errorMessageFromServer != null) {
            Text(
                text = errorMessageFromServer,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = { viewModel.login() },
            enabled = botonHabilitado,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color(0xFF2196F3),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onForgotPasswordClick() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "¿No eres miembro?", fontSize = 15.sp)

        Text(
            text = "Regístrate ahora",
            color = Color(0xFF2196F3),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onRegisterClick() }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text(text = " o ", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.g),
                    contentDescription = "Google Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Continuar con Google", color = Color.Black)
            }
        }
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
    leadingIcon: (@Composable (() -> Unit))? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
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
}