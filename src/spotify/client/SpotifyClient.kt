package dev.timkante.badgeK.spotify.client

import dev.timkante.badgeK.spotify.client.payloads.AccessToken
import dev.timkante.badgeK.spotify.client.payloads.CurrentlyPlayingResponse
import dev.timkante.badgeK.spotify.client.payloads.GetAuthTokenRequestBody
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.util.*

data class SpotifyClient internal constructor(
    private val client: HttpClient,
    private val clientId: String,
    private val secret: String,
    private val refreshToken: String,
    private val apiBaseUrl: String,
    private val authTokenBaseUrl: String,
) {
    private val basicAuthHeader: String by lazy {
        "Basic ${Base64.getEncoder().encodeToString("$clientId:$secret".toByteArray())}"
    }

    suspend fun getAuthToken(): AccessToken = client.post(authTokenBaseUrl) {
        header(HttpHeaders.Authorization, basicAuthHeader)
        header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
        body = GetAuthTokenRequestBody(refreshToken = refreshToken).formUrlEncoded
    }

    suspend fun getCurrentlyPlayedTitle(
        credentials: AccessToken,
    ): CurrentlyPlayingResponse = client.get<HttpStatement>("$apiBaseUrl$NOW_PLAYING_PATH") {
        header(HttpHeaders.Authorization, credentials.asAuthHeader)
    }.execute { response: HttpResponse ->
        when (response.status) {
            HttpStatusCode.OK -> response.receive()
            else -> CurrentlyPlayingResponse()
        }
    }

    suspend fun fetchImage(url: String) = client.get<ByteArray>(url)

    companion object Factory {
        private const val NOW_PLAYING_PATH = "/me/player/currently-playing"

        private val jsonWithDefaults = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        fun spotifyClient(
            baseUrl: String,
            clientId: String,
            secret: String,
            refreshToken: String,
            authTokenBaseUrl: String,
        ): SpotifyClient = SpotifyClient(
            HttpClient(Apache) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(jsonWithDefaults)
                }
            },
            clientId = clientId,
            secret = secret,
            refreshToken = refreshToken,
            apiBaseUrl = baseUrl,
            authTokenBaseUrl = authTokenBaseUrl,
        )
    }
}