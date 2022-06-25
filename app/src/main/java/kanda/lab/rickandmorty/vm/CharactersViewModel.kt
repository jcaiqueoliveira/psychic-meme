package kanda.lab.rickandmorty.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanda.lab.rickandmorty.StateMachine
import kanda.lab.rickandmorty.data.RickAndMortyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    private val service: RickAndMortyService
) : ViewModel() {

    private val _uiState = MutableStateFlow<StateMachine>(StateMachine.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        characters()
    }

    fun retry() = characters()

    private fun characters() {
        viewModelScope.launch {
            service.listCharacters()
                .also {
                    _uiState.emit(StateMachine.Success(it))
                }
        }
    }
}