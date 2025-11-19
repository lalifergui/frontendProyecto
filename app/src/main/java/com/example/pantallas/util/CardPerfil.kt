package com.example.pantallas.util
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantallas.modelos.Perfil

@Composable
fun CardPerfil(perfil: Perfil) {

    // Definimos un color azul oscuro similar al de la imagen
    val FondoCardColor = Color(0xFF2E4053)
    val TextoCardColor = Color.White

    // Contenedor principal: Rectángulo con fondo de color
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Fondo de color y bordes redondeados
            .background(FondoCardColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. Círculo de Foto de Perfil
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, TextoCardColor, CircleShape), // Borde blanco opcional
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Foto",
                color = Color.Black,
                fontSize = 14.sp
            )
            // Podrías usar Icons.Filled.AccountCircle si prefieres un icono
        }

        Spacer(modifier = Modifier.size(16.dp))

        // 2. Columna de Datos
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            // Nombre de usuario (usamos el email del objeto Usuario como ejemplo de identificador)
            Text(
                text = "Nombre usuario:\n${perfil.nombre} ${perfil.apellidos}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoCardColor // Texto blanco
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Edad (Usamos la fecha de nacimiento como dato proxy, idealmente calcularías la edad real)
            Text(
                text = "Edad: ${perfil.fechaNacimiento}", // Mostrará la fecha de nacimiento
                fontSize = 16.sp,
                color = TextoCardColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ciudad
            Text(
                text = "Ciudad: ${perfil.ciudad}",
                fontSize = 16.sp,
                color = TextoCardColor
            )
        }
    }
}