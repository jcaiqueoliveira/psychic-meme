package kanda.lab.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        vm.characters()
        setContent {
            Characters(vm)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun Characters(model: CharactersViewModel) {
        val characters by model.uiState.collectAsState()
        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = characters) {
                CharacterItem(character = it)
            }
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
            modifier = Modifier.size(130.dp),
            painter = rememberImagePainter(character.image),
            contentDescription = "Image from character ${character.name}",
        )
        Text(
            text = character.name,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {

    }
}
