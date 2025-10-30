package com.example.pantallas.ui.registro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lint.kotlin.metadata.Visibility

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
    /**
     * //para el calendario de la fecha de nacimiento
     *     val contexto = LocalContext.current
     *     val calendario = Calendar.getInstance()
     */

    val nombre by viewModel.nombre.collectAsState()
    val apellidos by viewModel.apellidos.collectAsState()
    val fechaNacimiento by viewModel.fechaNacimiento.collectAsState()

    val ciudad by viewModel.ciudad.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    //creamos estado para controlar la visibilidad
    var passwordVisible by remember { mutableStateOf(false) }


    /**
     * Utiliza un solo codigo ejercicio de formularios de kotllin
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Crear Usuario",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
                value = nombre,
        onValueChange = {viewModel.actualizarNombre(it) },
        label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = apellidos,
            onValueChange = {viewModel.actualizarApellidos(it) },
            label = { Text("Apellidos") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {viewModel.actualizarFechaNacimiento(it) },
            label = { Text("Fecha de Nacimiento") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = ciudad,
            onValueChange = {viewModel.actualizarCiudad(it) },
            label = { Text("Ciudad") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {viewModel.actualizarEmail(it) },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {viewModel.actualizarPassword(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        //como tenía antes la contraseña
        /**
         *  OutlinedTextField(
         *             value = password,
         *             onValueChange = { password = it },
         *             label = { Text("Contraseña") },
         *             visualTransformation = PasswordVisualTransformation(),
         *             modifier = Modifier.fillMaxWidth()
         *         )
         */


        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRegistrarClick) {
            Text("Registrar")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreen() {
    PantallaPrincipal()
}
