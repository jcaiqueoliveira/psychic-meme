package kanda.lab.rickandmorty.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RickAndMortyInfraStructure @Inject constructor(
    private val gateway: RickAndMortyGateway
) : RickAndMortyService {

    override suspend fun listCharacters(): List<Character> = withContext(Dispatchers.IO) {
        gateway.getCharacters().results
    }
}