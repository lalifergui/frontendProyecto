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

    val FondoCardColor = Color(0xFF2E4053)
    val TextoCardColor = Color.White

    // Contenedor principal: Rectángulo con fondo de color
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Fondo de color y bordes redondeados
            .background(FondoCardColor, shape = RoundedCornerShape(10.dp))
            // CLAVE: Reducción del padding vertical
            .padding(vertical = 8.dp, horizontal = 12.dp), // REDUCIDO de 10.dp a 8.dp
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. Círculo de Foto de Perfil
        Box(
            modifier = Modifier
                .size(68.dp) // REDUCIDO Ligeramente de 72.dp a 68.dp
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, TextoCardColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Foto",
                color = Color.Black,
                fontSize = 12.sp // Texto de foto ligeramente más pequeño
            )
        }

        Spacer(modifier = Modifier.size(10.dp)) // REDUCIDO de 16.dp a 12.dp

        // 2. Columna de Datos
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Nombre de usuario y Apellidos (El texto que ocupa más altura)
            Text(
                text = "\n${perfil.nombre} ${perfil.apellidos}",
                fontSize = 14.sp, // Reducido de 18.sp a 14.sp para evitar desborde/altura
                fontWeight = FontWeight.SemiBold,
                color = TextoCardColor,
                lineHeight = 16.sp, // Controlamos el interlineado para compactar
            )

            Spacer(modifier = Modifier.height(2.dp)) // REDUCIDO de 4.dp a 2.dp

            // Edad
            Text(
                text = "Edad: ${perfil.fechaNacimiento}",
                fontSize = 12.sp, // Reducido de 16.sp a 12.sp
                color = TextoCardColor
            )

            Spacer(modifier = Modifier.height(2.dp)) // REDUCIDO de 4.dp a 2.dp

            // Ciudad
            Text(
                text = "Ciudad: ${perfil.ciudad}",
                fontSize = 12.sp, // Reducido de 16.sp a 12.sp
                color = TextoCardColor
            )
        }
    }
}