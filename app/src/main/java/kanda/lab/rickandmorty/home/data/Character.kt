package kanda.lab.rickandmorty.home.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
internal data class Character(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val image: String
) : java.io.Serializable, Parcelable