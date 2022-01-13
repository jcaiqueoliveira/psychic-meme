package kanda.lab.rickandmorty.data

import kotlinx.serialization.Serializable

@Serializable
internal data class Result(
    val results: List<Character>
)