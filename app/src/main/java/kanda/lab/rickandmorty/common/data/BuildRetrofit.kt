@file:OptIn(ExperimentalSerializationApi::class)

package kanda.lab.rickandmorty.common.data

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File

internal object BuildRetrofit {

    operator fun invoke(application: Application): Retrofit {
        val cache = with(application) { getCacheConfig() }
        val okHttp = OkHttpClient.Builder()
            .cache(cache)
            .build()

        return with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(okHttp)
            addConverterFactory(JsonSupport.relaxed.asConverterFactory(contentType))
            build()
        }
    }

    private fun Application.getCacheConfig(): Cache =
        Cache(
            directory = File(cacheDir, "http_cache"),
            maxSize = 20L * 1024L * 1024L // 20 MiB
        )

    private val contentType by lazy {
        "application/json".toMediaTypeOrNull()!!
    }
    private const val apiURL: String = "https://rickandmortyapi.com/api/"
}

private object JsonSupport {
    val relaxed by lazy {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}
