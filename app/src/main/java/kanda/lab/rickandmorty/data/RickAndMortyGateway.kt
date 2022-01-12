package kanda.lab.rickandmorty.data

import retrofit2.http.GET

interface RickAndMortyGateway {

    @GET("character")
    suspend fun getCharacters() : Result
}