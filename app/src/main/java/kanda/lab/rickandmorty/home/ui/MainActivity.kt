@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package kanda.lab.rickandmorty.home.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import kanda.lab.rickandmorty.LoadingAnimation
import kanda.lab.rickandmorty.R
import kanda.lab.rickandmorty.common.ui.states.StateMachine
import kanda.lab.rickandmorty.common.theme.RickandmortyTheme
import kanda.lab.rickandmorty.home.data.Character
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(vm)
        }
    }
}

@Composable
private fun HomeScreen(vm: CharactersViewModel) {
    val state by vm.uiState.collectAsState()
    when (state) {
        is StateMachine.Error -> Error(vm::retry)
        StateMachine.Loading -> Loading()
        is StateMachine.Success -> GridCharacters(state as StateMachine.Success)
    }
}

@Composable
private fun Error(retry: () -> Unit) {
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

@Composable
private fun GridCharacters(success: StateMachine.Success) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val char = remember { mutableStateOf(success.characters.first()) }

    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Text(
                    text = char.value.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = char.value.species,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.White
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = success.characters) {
                CharacterItem(character = it) {
                    coroutineScope.launch {
                        char.value = it
                        if (sheetState.isVisible) sheetState.hide() else sheetState.show()
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(character: Character, detail: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                detail.invoke()
            },
    ) {
        Image(
            modifier = Modifier
                .size(130.dp)
                .fillMaxSize(),
            painter = rememberImagePainter(character.image),
            contentDescription = "Image from character ${character.name}",
        )
        Text(
            text = character.name,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun Loading() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LoadingAnimation(circleColor = Color.Green.copy(alpha = 0.8f))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {
        Loading()
    }
}
