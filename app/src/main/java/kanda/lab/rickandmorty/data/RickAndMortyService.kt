package kanda.lab.rickandmorty.data

interface  RickAndMortyService {

    suspend fun listCharacters() : List<Character>
}