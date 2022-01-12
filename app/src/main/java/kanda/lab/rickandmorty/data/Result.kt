package kanda.lab.rickandmorty.data

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val results: List<Character>
)