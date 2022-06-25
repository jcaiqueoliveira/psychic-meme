package kanda.lab.rickandmorty.common.ui.states

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kanda.lab.rickandmorty.R

@Composable
fun ErrorState(retry: () -> Unit) {
    val isPressed = remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You got an error", color = Color.Green, fontSize = 20.sp)
        Image(
            painter = painterResource(R.drawable.rick_error),
            contentDescription = "error server"
        )
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green.copy(alpha = 0.8f)),
            onClick = {
                if (isPressed.value.not()) {
                    isPressed.value = isPressed.value.not()
                    retry.invoke()
                }
            },
        ) {
            AnimatedVisibility(visible = isPressed.value) {
                if (isPressed.value) {
                    Row {
                        CircularProgressIndicator(
                            Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    }
                }
            }
            Text("Retry", color = Color.White)
        }
    }
}