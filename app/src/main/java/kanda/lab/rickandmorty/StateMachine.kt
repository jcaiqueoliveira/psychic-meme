package kanda.lab.rickandmorty

internal sealed class StateMachine() {
    object Loading : StateMachine()
    data class Success(val characters: List<kanda.lab.rickandmorty.data.Character>) : StateMachine()
    data class Error(val error: Throwable) : StateMachine()
}