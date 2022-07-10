package kanda.lab.rickandmorty.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanda.lab.rickandmorty.common.data.RickAndMortyInfraStructure
import kanda.lab.rickandmorty.common.ui.states.StateMachine
import kanda.lab.rickandmorty.home.data.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    private val service: RickAndMortyInfraStructure
) : ViewModel() {

    private val _uiState = MutableStateFlow<StateMachine>(StateMachine.Loading)
    val uiState = _uiState.asStateFlow()

    private var chararacter: Result<Character>? = null

    init {
        characters()
    }

    fun retry() = characters()

    fun charDetailSelected(char: Character) {
        chararacter = Result.success(char)
    }

    fun getSelectedChar() = chararacter


    private fun characters() {
        viewModelScope.launch {
            service.listCharacters()
                .also {
                    _uiState.emit(StateMachine.Success(it))
                }
        }
    }
}