package dev.timkante.badgeK.spotify.api

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.locations.get
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.text.toByteArray

@Location("/spotify/play-status")
data class PlayStatus(val redirect: Boolean = true) {

    companion object Routing {
        fun Route.playStatus() {
            get<PlayStatus> { (redirect) ->
                call.respondBytes {
                    "Works [redirect=$redirect]".toByteArray()
                }
            }
        }
    }
}