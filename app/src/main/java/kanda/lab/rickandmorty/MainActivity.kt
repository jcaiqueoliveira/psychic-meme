@file:OptIn(ExperimentalFoundationApi::class)

package kanda.lab.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import kanda.lab.rickandmorty.data.Character
import kanda.lab.rickandmorty.ui.theme.RickandmortyTheme
import kanda.lab.rickandmorty.vm.CharactersViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }

    @Composable
    private fun HomeScreen() {
        val state by vm.uiState.collectAsState()
        when (state) {
            is StateMachine.Error -> Error()
            StateMachine.Loading -> Loading()
            is StateMachine.Success -> GridCharacters(state as StateMachine.Success)
        }
    }
}

@Composable
private fun GridCharacters(success: StateMachine.Success) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = success.characters) {
            CharacterItem(character = it)
        }
    }
}

@Composable
private fun CharacterItem(character: Character) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { },
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

@Composable
private fun Error() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You got an error", color = Color.Green, fontSize = 20.sp)
        Image(painter = painterResource(R.drawable.rick_error), contentDescription = "error server")
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green.copy(alpha = 0.8f)),
            onClick = { }) {
            Text("Retry", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {
        Loading()
    }
}
