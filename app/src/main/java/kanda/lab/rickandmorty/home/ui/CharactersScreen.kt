@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package kanda.lab.rickandmorty.home.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import kanda.lab.rickandmorty.common.theme.RickandmortyTheme
import kanda.lab.rickandmorty.common.ui.states.ErrorState
import kanda.lab.rickandmorty.common.ui.states.LoadingState
import kanda.lab.rickandmorty.common.ui.states.StateMachine
import kanda.lab.rickandmorty.home.data.Character

@Composable
internal fun CharactersScreen(
    vm: CharactersViewModel = hiltViewModel(),
    navigateToDetail: (Character) -> Unit,
) {
    val state by vm.uiState.collectAsState()
    var toggleState by remember { mutableStateOf(0) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    when (state) {
        is StateMachine.Error -> ErrorState(vm::retry)
        StateMachine.Loading -> LoadingState()
        is StateMachine.Success -> {
            val characters = (state as StateMachine.Success).characters
            GridCharacters(
                characters = characters,
                onMoreDetailClicked = navigateToDetail,
                keyValue = toggleState,
                onKeyValueChange = { toggleState++ },
                sheetState = sheetState,
                getSelectedCharacter = { vm.getSelectedChar()?.getOrNull() },
                onCharacterSelected = { c -> vm.charDetailSelected(c) }
            )
        }
    }
}

@Composable
private fun GridCharacters(
    characters: List<Character>,
    onMoreDetailClicked: (Character) -> Unit,
    keyValue: Int,
    onKeyValueChange: () -> Unit,
    sheetState: ModalBottomSheetState,
    getSelectedCharacter: () -> Character?,
    onCharacterSelected: (Character) -> Unit
) {

    LaunchedEffect(key1 = keyValue) {
        if (keyValue != 0) {
            if (sheetState.isVisible) sheetState.hide() else sheetState.show()
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            SheetContent(
                modifier = Modifier
                    .height(250.dp)
                    .clip(RoundedCornerShape(10.dp)),
                getSelectedCharacter = getSelectedCharacter,
                onMoreDetailClicked = onMoreDetailClicked
            )
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        GridContent(
            characters = characters,
            toggleBottomSheet = { character ->
                onKeyValueChange.invoke()
                onCharacterSelected.invoke(character)
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
private fun SheetContent(
    modifier: Modifier,
    getSelectedCharacter: () -> Character?,
    onMoreDetailClicked: (Character) -> Unit
) {
    val char = getSelectedCharacter()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        if (char != null) {

            Image(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .width(180.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),
                painter = rememberAsyncImagePainter(char.image),
                contentDescription = "Image from character ${char.name}",
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                    .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                Text(
                    text = char.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )

                Text(
                    text = "${char.status} - ${char.species}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White

                )
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(8.dp),
                    onClick = { onMoreDetailClicked(char) },
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text("More details", color = Color.White)
                }
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
            painter = rememberAsyncImagePainter(character.image),
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