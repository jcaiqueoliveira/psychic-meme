package kanda.lab.rickandmorty.common.data

import kanda.lab.rickandmorty.home.data.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RickAndMortyInfraStructure @Inject constructor(
    private val gateway: RickAndMortyGateway
) {

    suspend fun listCharacters(): List<Character> = withContext(Dispatchers.IO) {
        gateway.getCharacters().results
    }
}