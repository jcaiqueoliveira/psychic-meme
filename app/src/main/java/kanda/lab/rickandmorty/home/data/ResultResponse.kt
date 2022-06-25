package kanda.lab.rickandmorty.home.data

import kotlinx.serialization.Serializable

@Serializable
internal data class ResultResponse(
    val results: List<Character>
)