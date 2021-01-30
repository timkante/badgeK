package dev.timkante.badgeK.spotify.api

import dev.timkante.badgeK.spotify.client.SpotifyClient
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.locations.get
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.text.toByteArray

@Location("/spotify/play-status")
data class PlayStatus(val redirect: Boolean = true) {

    companion object Routing {
        fun Route.playStatus(client: SpotifyClient) {
            get<PlayStatus> { (redirect) ->
                call.respondBytes {
                    val token = client.getAuthToken()
                    Json.encodeToString(client.getCurrentlyPlayedTitle(token)).toByteArray()
                }
            }
        }
    }
}