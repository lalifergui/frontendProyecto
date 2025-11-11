package com.example.pantallas.ui.registro

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import  com.example.pantallas.R
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel


class Registrar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Muestra tu pantalla aquí cuando ejecutes la app
            PantallaPrincipal()
        }
    }
}
@Composable
fun PantallaPrincipal(viewModel: RegistroViewModel=viewModel()){
    RegistroScreen(viewModel)
}


@Composable
fun RegistroScreen(
    //una funcion no recibe parámetros y no devuelve nada

    viewModel: RegistroViewModel,
    onRegistrarClick: () -> Unit = {}
) {

    val usuario by viewModel.usuario.collectAsState()
    val errorEmail by viewModel.errorEmail.collectAsState()
    val errorPassword by viewModel.errorPassword.collectAsState()

    val botonHabilitado by viewModel.botonHabilitado.collectAsState(initial = false)

    var passwordVisible by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Image(
            painter = painterResource(id=R.drawable.biblio),
            contentDescription = "Logo Biblioswipe",
            modifier = Modifier.height(150.dp).width(150.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Crear Usuario",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        MiTextField(
            value = usuario.usuario,
            onValueChange ={nuevoNombre->viewModel.actualizarUsuario(usuario.copy(usuario = nuevoNombre))},
            label = "Nombre",
            leadingIcon = {Icon(Icons.Default.Person, contentDescription = "Icono nombre")}

        )
        Spacer(modifier = Modifier.height(20.dp))
        MiTextField(
            value = usuario.apellidos,
            onValueChange ={nuevoApellidos->viewModel.actualizarUsuario(usuario.copy(apellidos = nuevoApellidos))},
            label = "Apellidos",
            leadingIcon = {Icon(Icons.Default.Person, contentDescription = "Icono apellidos")}

        )
        Spacer(modifier = Modifier.height(20.dp))
        MiTextField(
            value = usuario.fechaNacimiento,
            onValueChange ={nuevaFecha->viewModel.actualizarUsuario(usuario.copy(fechaNacimiento = nuevaFecha))},
            label = "Fecha Nacimiento",
            leadingIcon = {Icon(Icons.Default.DateRange, contentDescription = "Icono fecha nacimiento")}

        )
        Spacer(modifier = Modifier.height(20.dp))
        MiTextField(
            value = usuario.ciudad,
            onValueChange ={nuevaCiudad->viewModel.actualizarUsuario(usuario.copy(ciudad = nuevaCiudad))},
            label = "Ciudad",
            leadingIcon = {Icon(Icons.Default.Home, contentDescription = "Icono Ciudad")}

        )
        Spacer(modifier = Modifier.height(20.dp))
        MiTextField(
            value = usuario.email,
            onValueChange ={nuevoEmail->viewModel.actualizarUsuario(usuario.copy(email = nuevoEmail))},
            label = "Email",
            leadingIcon = {Icon(Icons.Default.Email, contentDescription = "Icono Email")}
        )
        if (errorEmail) {
            Text(
                text = "Error, email incorrecto.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = usuario.password,
            onValueChange ={nuevoPassword->viewModel.actualizarUsuario(usuario.copy(password = nuevoPassword))},
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
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
        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRegistrarClick) {
            Text("Registrar")
        }
    }
}
@Composable
fun MiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    //isError: Boolean,
    //errorMessage: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: (@Composable (() -> Unit))? = null,
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        //isError = isError,
        keyboardOptions = keyboardOptions,
        leadingIcon=leadingIcon
    )

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreen() {
    PantallaPrincipal()
}
