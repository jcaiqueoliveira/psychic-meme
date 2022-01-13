package kanda.lab.rickandmorty.vm

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanda.lab.rickandmorty.data.Character
import kanda.lab.rickandmorty.data.RickAndMortyInfraStructure
import kanda.lab.rickandmorty.data.RickAndMortyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    private val service: RickAndMortyService
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Character>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun characters() {
        viewModelScope.launch {
            service.listCharacters()
                .also {
                    Log.e("size", it.size.toString())
                    _uiState.emit(it)
                }
        }
    }
}