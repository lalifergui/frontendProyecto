package com.example.pantallas.pantallas

import android.content.Intent
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class Principal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrincipalScreen()
        }
    }
}

@Composable
fun PrincipalScreen() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Caja principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Borde del campo
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            // Título
            Text(
                text = "Biblio-Swipe",
                color = Color.Black,
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.White)
                    .padding(start = 12.dp, end = 12.dp)
                    .offset(y = (-10).dp)
            )

            // Campo de texto interno (placeholder)
            BasicTextField(
                value = nombre,
                onValueChange = { nombre = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 20.dp, bottom = 600.dp)
                    .align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Barra inferior con iconos centrados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Casa",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            val intent = Intent(context, Principal::class.java)
                            context.startActivity(intent)
                        }
                )
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Match",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            val intent = Intent(context, Favoritos::class.java)
                            context.startActivity(intent)
                        }
                )
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "LibrosGuardados",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = "Chat",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            val intent = Intent(context, Perfil::class.java)
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() {
    PrincipalScreen()
}
