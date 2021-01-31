package dev.timkante.badgeK

import dev.timkante.badgeK.common.modules.registerModule
import dev.timkante.badgeK.spotify.SpotifyModule
import dev.timkante.badgeK.spotify.client.SpotifyClient
import dev.timkante.badgeK.spotify.client.SpotifyClient.Factory.spotifyClient
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(
    testing: Boolean = false,
    spotifyClient: SpotifyClient = with(ApplicationConfig::requireProperty) {
        spotifyClient(
            baseUrl = this("spotify.client.baseUrl"),
            clientId = this("spotify.client.clientId"),
            secret = this("spotify.client.secret"),
            refreshToken = this("spotify.client.refreshToken"),
            authTokenBaseUrl = this("spotify.client.authTokenBaseUrl"),
        )
    },
) {
    install(Locations) {}

    routing {
        registerModule(SpotifyModule(spotifyClient))
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}
