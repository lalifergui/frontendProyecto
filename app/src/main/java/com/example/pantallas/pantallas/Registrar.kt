package com.example.pantallas.pantallas

//import android.app.DatePickerDialog

import android.os.Bundle
//import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

//import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.core.view.WindowCompat.enableEdgeToEdge
//import java.util.Calendar

class Registrar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Muestra tu pantalla aquí cuando ejecutes la app
            RegistroScreen()
        }
    }
}

@Composable
fun PantallaConTextEditable(
    valor: String,
    onTextFieldChange: (String)->Unit,
    etiqueta:String

){
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        value = valor,
        onValueChange = onTextFieldChange,
        label = {Text(etiqueta)},
        modifier = Modifier.fillMaxWidth()


    )

    //Text(text =nombre)

}

@Composable
fun RegistroScreen(
    //una funcion no recibe parámetros y no devuelve nada
    onRegistrarClick: () -> Unit = {}
) {
    //para el calendario de la fecha de nacimiento
    //val contexto = LocalContext.current
    //val calendario = Calendar.getInstance()

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        PantallaConTextEditable(nombre,{nombre=it},"Nombre")
        PantallaConTextEditable(apellidos,{apellidos=it},"Apellidos")
        PantallaConTextEditable(fechaNacimiento,{fechaNacimiento=it},"Fecha de Nacimiento")
        PantallaConTextEditable(ciudad,{ciudad=it},"Ciudad")
        PantallaConTextEditable(email,{email=it},"Email")
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRegistrarClick) {
            Text("Registrar")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreen() {
    RegistroScreen()
}
