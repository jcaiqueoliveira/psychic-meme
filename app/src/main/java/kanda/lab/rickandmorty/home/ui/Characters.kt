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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

@Composable
internal fun CharactersScreen(
    vm: CharactersViewModel = hiltViewModel(),
    onDetailSelected: () -> Unit,
) {
    val state by vm.uiState.collectAsState()
    when (state) {
        is StateMachine.Error -> ErrorState(vm::retry)
        StateMachine.Loading -> LoadingState()
        is StateMachine.Success -> {
            val characters = (state as StateMachine.Success).characters
            GridCharacters(
                characters = characters,
                onMoreDetailClicked = onDetailSelected,
            )
        }
    }
}

@Composable
private fun GridCharacters(
    characters: List<Character>,
    onMoreDetailClicked: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val characterState = rememberSaveable { mutableStateOf(characters.first()) }
    val toggleState = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = characterState.value, key2 = toggleState.value) {
        if (toggleState.value != 0) {
            if (sheetState.isVisible) sheetState.hide() else sheetState.show()
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
        sheetContent = {
            SheetContent(
                modifier = Modifier.height(250.dp),
                char = characterState.value,
                onMoreDetailClicked = onMoreDetailClicked
            )
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.DarkGray
    ) {
        GridContent(
            characters = characters,
            toggleBottomSheet = { character ->
                toggleState.value++
                characterState.value = character
            }
        )
    }
}

@Composable
private fun GridContent(
    characters: List<Character>,
    toggleBottomSheet: (Character) -> Unit,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = characters) {
            CharacterItem(character = it, detail = { toggleBottomSheet(it) })
        }
    }
}

@Composable
private fun SheetContent(modifier: Modifier, char: Character, onMoreDetailClicked: () -> Unit) {
    Row(modifier = modifier.fillMaxWidth()) {

        Image(
            modifier = modifier
                .width(180.dp)
                .fillMaxHeight(),
            painter = rememberImagePainter(char.image),
            contentDescription = "Image from character ${char.name}",
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = char.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = char.species,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
            )

            OutlinedButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onMoreDetailClicked,
            ) {
                Text("More details", color = Color.Black)
            }
        }
    }
}

@Composable
private fun CharacterItem(character: Character, detail: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
    ) {
        Image(
            modifier = Modifier
                .size(130.dp)
                .fillMaxSize()
                .clickable { detail.invoke() },
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