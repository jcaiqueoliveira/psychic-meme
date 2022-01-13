package kanda.lab.rickandmorty.data

import retrofit2.http.GET

internal interface RickAndMortyGateway {

    @GET("character")
    suspend fun getCharacters() : Result
}