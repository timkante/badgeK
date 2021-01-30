package dev.timkante.badgeK.spotify

import dev.timkante.badgeK.common.modules.Module
import dev.timkante.badgeK.spotify.api.PlayStatus
import dev.timkante.badgeK.spotify.client.SpotifyClient
import io.ktor.routing.*

data class SpotifyModule(val client: SpotifyClient) : Module {
    override fun Routing.register() {
        with(PlayStatus.Routing) {
            playStatus(client = client)
        }
    }
}