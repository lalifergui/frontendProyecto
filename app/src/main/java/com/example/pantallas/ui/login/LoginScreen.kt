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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
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

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaLogin(
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
    onRegisterClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val loginResult by viewModel.loginResult.collectAsState()

    // Navegación al tener éxito con MySQL
    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            val intent = Intent(context, Principal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    LoginScreen(
        viewModel = viewModel,
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    // 🎯 Observamos variables individuales según tu ViewModel profesional
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
            painter = painterResource(id = R.drawable.biblio),
            contentDescription = "Logo biblioswipe",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text(text = "Log in", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(40.dp))

        // 🎯 Campo amoldado a onEmailChanged
        MiTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = "Email",
            isError = errorEmail,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Icono Email") },
            errorMessage = "Email inválido."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🎯 Campo amoldado a onPasswordChanged
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                Icon(
                    imageVector = image,
                    contentDescription = null,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorPassword,
            leadingIcon = { Icon(Icons.Default.Password, contentDescription = "Icono Contraseña") },
            singleLine = true,
            supportingText = {
                if (errorPassword) {
                    Text(text = "Mínimo 4 caracteres.", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.login() },
            enabled = botonHabilitado,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Log in", color = Color.White)
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
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    PantallaLogin()
}