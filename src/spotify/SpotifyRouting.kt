package dev.timkante.badgeK.spotify

import dev.timkante.badgeK.spotify.api.PlayStatus
import io.ktor.routing.*

object SpotifyRouting {
    fun Routing.registerModule() {
        with(PlayStatus.Routing) {
            playStatus()
        }
    }
}