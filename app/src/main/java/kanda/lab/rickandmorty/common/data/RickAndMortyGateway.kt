package kanda.lab.rickandmorty.common.data

import kanda.lab.rickandmorty.home.data.ResultResponse
import retrofit2.http.GET

internal interface RickAndMortyGateway {

    @GET("character")
    suspend fun getCharacters() : ResultResponse
}