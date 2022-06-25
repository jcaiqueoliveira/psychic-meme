@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package kanda.lab.rickandmorty.home.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import kanda.lab.rickandmorty.common.theme.RickandmortyTheme
import kanda.lab.rickandmorty.common.ui.states.ErrorState
import kanda.lab.rickandmorty.common.ui.states.LoadingState
import kanda.lab.rickandmorty.common.ui.states.StateMachine
import kanda.lab.rickandmorty.home.data.Character
import kotlinx.coroutines.launch

@Composable
internal fun CharactersScreen(
    vm: CharactersViewModel = hiltViewModel(),
    onDetailSelected: () -> Unit,
) {
    val state by vm.uiState.collectAsState()
    when (state) {
        is StateMachine.Error -> ErrorState(vm::retry)
        StateMachine.Loading -> LoadingState()
        is StateMachine.Success -> GridCharacters(
            (state as StateMachine.Success).characters,
            onDetailSelected
        )
    }
}

@Composable
private fun GridCharacters(characters: List<Character>, onClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val char = remember { mutableStateOf(characters.first()) }

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

                Button(onClick = onClick) {
                    Text("Click to move", color = Color.Green)
                }
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
            items(items = characters) {
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RickandmortyTheme {
        LoadingState()
    }
}