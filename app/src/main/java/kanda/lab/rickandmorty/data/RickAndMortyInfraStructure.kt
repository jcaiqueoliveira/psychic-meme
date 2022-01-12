package kanda.lab.rickandmorty.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RickAndMortyInfraStructure(
    private val gateway: RickAndMortyGateway = BuildRetrofit().create(RickAndMortyGateway::class.java)
) : RickAndMortyService {

    override suspend fun listCharacters(): List<Character> = withContext(Dispatchers.IO) {
        gateway.getCharacters().results
    }
}