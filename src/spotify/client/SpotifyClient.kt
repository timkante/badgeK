package dev.timkante.badgeK.spotify.client

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

data class SpotifyClient internal constructor(
    private val client: HttpClient,
    private val clientId: String,
    private val secret: String,
    private val refreshToken: String,
) {

    companion object Factory {
        fun spotifyClient(
            baseUrl: String,
            clientId: String,
            secret: String,
            refreshToken: String,
        ): SpotifyClient = SpotifyClient(
            HttpClient(Apache) {
                defaultRequest {
                    method = HttpMethod.Get
                    host = baseUrl
                }
                followRedirects = true
            },
            clientId = clientId,
            secret = secret,
            refreshToken = refreshToken,
        )
    }
}