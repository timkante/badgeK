package dev.timkante.badgeK.spotify.api

import dev.timkante.badgeK.spotify.client.SpotifyClient
import io.ktor.application.*
import io.ktor.http.*
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
                val token = client.getAuthToken()
                val currentSong = client.getCurrentlyPlayedTitle(token)
                if (redirect) {
                    currentSong.item?.let { track ->
                        call.respondRedirect(url = track.externalUrls.spotify)
                    } ?: call.respond(HttpStatusCode.OK)
                } else {
                    call.respondBytes {
                        Json.encodeToString(client.getCurrentlyPlayedTitle(token)).toByteArray()
                    }
                }
            }
        }
    }
}