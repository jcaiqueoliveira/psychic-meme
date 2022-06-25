package kanda.lab.rickandmorty.home.data

import kotlinx.serialization.Serializable

@Serializable
internal data class Character(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val image: String
) : java.io.Serializable