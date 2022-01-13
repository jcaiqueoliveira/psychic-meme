package kanda.lab.rickandmorty.data

internal interface RickAndMortyService {

    suspend fun listCharacters(): List<Character>
}