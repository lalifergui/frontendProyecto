package com.example.pantallas.ui.principal

import android.content.Intent
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.pantallas.ui.favoritos.Favoritos
import com.example.pantallas.util.Menu
import com.example.pantallas.R


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
fun PrincipalScreen(

) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
            ){
                //Dentro de la caja principal
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        //para que quede por encima del borde
                        .offset(y=(-12).dp)
                        .padding(horizontal = 16.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.CenterStart
                ){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        TextButton (
                            onClick = {
                                val intent = Intent(context, Favoritos::class.java)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .height(60.dp)
                        ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Icon(
                                        painter= painterResource(id = R.drawable.libro_tick),
                                        contentDescription = "Me gusta",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                val intent = Intent(context, Principal::class.java)
                                context.startActivity(intent) },
                            modifier = Modifier
                                .padding(16.dp)
                                .height(60.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    painter= painterResource(id = R.drawable.libro_x),
                                    contentDescription = "No me gusta",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }

                    }
                }

            }

            // Título
            Text(
                text = "BIBLIO-SWIPE",
                color = Color.Black,
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.White)
                    .padding(start = 12.dp, end = 12.dp)

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
        Menu(context)

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPrincipalScreen() {
    PrincipalScreen()
}
