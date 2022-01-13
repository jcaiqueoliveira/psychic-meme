package kanda.lab.rickandmorty.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit

internal object BuildRetrofit {

    operator fun invoke(
        apiURL: String = "https://rickandmortyapi.com/api/",
        httpClient: OkHttpClient = OkHttpClient()
    ): Retrofit =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(httpClient)
            addConverterFactory(JsonSupport.relaxed.asConverterFactory(contentType))
            build()
        }

    private val contentType by lazy {
        "application/json".toMediaTypeOrNull()!!
    }
}

private object JsonSupport {
    val relaxed by lazy {
        Json {
            allowSpecialFloatingPointValues = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
            useArrayPolymorphism = true
        }
    }
}
