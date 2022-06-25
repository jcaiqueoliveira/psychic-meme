package kanda.lab.rickandmorty.common.ui.states

import kanda.lab.rickandmorty.home.data.Character

internal sealed class StateMachine() {
    object Loading : StateMachine()
    data class Success(val characters: List<Character>) : StateMachine()
    data class Error(val error: Throwable) : StateMachine()
}