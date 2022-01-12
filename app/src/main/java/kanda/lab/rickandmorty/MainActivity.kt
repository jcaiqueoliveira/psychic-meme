package kanda.lab.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanda.lab.rickandmorty.data.Character
import kanda.lab.rickandmorty.ui.theme.RickandmortyTheme
import kanda.lab.rickandmorty.vm.CharactersViewModel
import java.util.*

class MainActivity : ComponentActivity() {
    private val vm = CharactersViewModel()
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
    fun Characters(model: CharactersViewModel) {
        val characters by model.uiState.collectAsState()

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
        ) {
            items(characters) { character ->
                Greeting(name = character.name)
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {
        Greeting("Android")
    }
}
