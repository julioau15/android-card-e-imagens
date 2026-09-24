package com.aulasandroid.card_e_imagens

import android.R.attr.fontWeight
import android.R.attr.text
import android.R.attr.value
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log.i
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aulasandroid.card_e_imagens.ui.theme.Card_e_imagensTheme
import kotlin.isNaN

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Card_e_imagensTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Preview
@Composable
fun IMCScreen(modifier: Modifier = Modifier) {
    val teclado = LocalSoftwareKeyboardController.current

    val corPesoIdeal = Color(37, 146, 52, 255)
    val corLevementeAcima = Color(255, 152, 0, 255)
    val corAbaxoObesidade = Color(186, 52, 40, 255)

    var corCard by remember {
        mutableStateOf(corPesoIdeal)
    }

    var textoCard by remember {
        mutableStateOf("")
    }

    var mostrarCard by remember {
        mutableStateOf(false)
    }

    var altura by remember {
        mutableStateOf("")
    }

    var peso by remember {
        mutableStateOf("")
    }

    var IMC by remember {
        mutableStateOf(0.0)
    }

    var IMCFormatado by remember {
        mutableStateOf("")
    }

    Column(modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxSize().background(Color.White)){

            Column(modifier = Modifier.fillMaxWidth()) {

                // -- Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(colorResource(R.color.cor_app)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.bmi),
                        contentDescription = "icone BMI",
                        modifier = Modifier.size(80.dp)
                            .padding(vertical = 16.dp)
                    )

                    Text(
                        text = "Calculadora IMC",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // -- Formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .offset(y = (-30).dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)

                ) {
                    Text(
                        text = "Seus dados",
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.cor_app),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // -- Input altura
                    OutlinedTextField(
                        value = altura,
                        onValueChange = { novaAltura ->
                            // Permite apenas se todos os caracteres forem números (ou se estiver vazio)
                            if (novaAltura.all { it.isDigit() || it == '.' } ) {
                                altura = novaAltura
                            }
                        },
                        placeholder = {
                            Text("Altura")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(R.color.cor_app)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )

                    // -- Input Peso
                    OutlinedTextField(
                        value = "$peso",
                        onValueChange = { novoPeso ->
                            // Permite apenas se todos os caracteres forem números (ou se estiver vazio)
                            if (novoPeso.all { it.isDigit() || it == '.' }) {
                                peso = novoPeso
                            }
                        },
                        placeholder = {
                            Text("Peso")
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(R.color.cor_app)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )

                    // -- Botão de calcualr
                    Button(
                        onClick = {

                            if(altura != "" && peso != "" ){
                                val IMC = calcularIMC(altura.toDouble(), peso.toDouble())

                                when {
                                    IMC < 18.5 -> {
                                        textoCard = "Abaixo do peso"
                                        corCard = corAbaxoObesidade
                                    }

                                    IMC < 25 -> {
                                        textoCard = "Peso ideal."
                                        corCard = corPesoIdeal
                                    }

                                    IMC < 30 -> {
                                        textoCard = "Levemente acima do peso."
                                        corCard = corLevementeAcima
                                    }

                                    IMC < 35 -> {
                                        textoCard = "Obesidade grau I."
                                        corCard = corAbaxoObesidade
                                    }

                                    IMC < 40 -> {
                                        textoCard = "Obesidade grau II."
                                        corCard = corAbaxoObesidade
                                    }

                                    else -> {
                                        textoCard = "Obesidade grau III."
                                        corCard = corAbaxoObesidade
                                    }
                                }

                                IMCFormatado = String.format("%.2f", IMC)
                                mostrarCard = true
                                teclado?.hide()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.cor_app)
                        )
                    ) {
                        Text("CALCULAR")
                    }

                    if (mostrarCard){
                        // -- Botão de Limpar tela
                        Button(
                            onClick = {
                                altura = ""
                                peso = ""
                                mostrarCard = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray
                            )
                        ) {
                            Text("LIMPAR TELA")
                        }
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                // card resultado

                if(mostrarCard) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = corCard
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = IMCFormatado,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.size(20.dp))

                            Text(
                                text = "$textoCard",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

fun calcularIMC(altura: Double, peso: Double): Double{
    try {
        val IMC = peso / (altura * altura)
        return IMC
    } catch (error: Error){
        return 0.0
    }
}

