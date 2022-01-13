package kanda.lab.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            RickandmortyTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Characters(vm)
                }
            }
        }
    }

    @Composable
    private fun Characters(model: CharactersViewModel) {
        val characters by model.uiState.collectAsState()

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
        ) {
            items(characters) { character ->
                CharacterItem(character = character)
            }
        }
    }
}

@Composable
private fun CharacterItem(character: Character) {
    Row(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(character.image),
            contentDescription = "Image from character named as ${character.name}",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = character.name
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {

    }
}
