package dev.timkante.badgeK.spotify.api

import dev.timkante.badgeK.spotify.client.SpotifyClient
import dev.timkante.badgeK.spotify.client.payloads.Artist
import dev.timkante.badgeK.spotify.components.ReadmeImage
import dev.timkante.badgeK.spotify.components.styles.FontWeights
import dev.timkante.badgeK.spotify.components.styles.TextColors
import dev.timkante.badgeK.spotify.components.styles.TextSize
import dev.timkante.badgeK.spotify.components.textFormatted
import dev.timkante.badgeK.styleCss
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.get
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.text.toByteArray

@Location("/spotify/play-status")
data class PlayStatus(val redirect: Boolean = false) {

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
                    val imageData = currentSong.item?.album?.images?.last()?.url?.let { location ->
                        Base64.getEncoder().encodeToString(client.fetchImage(location))
                    }
                    val coverImage by lazy { "data:image/jpeg;base64,$imageData" }
                    val body = (ReadmeImage(width = 540, height = 64)) {
                        style {
                            style {
                                +mainCssFor(currentSong)
                            }
                        }
                        children {
                            div(classes = if (currentSong.playing) "disabled" else "") {
                                attributes += "style" to CSSBuilder().apply {
                                    display = Display.flex
                                    alignItems = Align.center
                                    paddingTop = 8.px
                                    paddingLeft = 4.px
                                }.toString()

                                textFormatted(
                                    weight = FontWeights.BOLD,
                                    size = TextSize.LARGE,
                                    extraStyles = {
                                        width = 16.px
                                        marginRight = 16.px
                                    },
                                ) {
                                    +if (currentSong.playing) "▶" else ""
                                }

                                img(src = if (imageData != null) coverImage else null) {
                                    width = "48"
                                    height = "48"
                                    id = "cover"
                                }

                                div {
                                    attributes += "style" to CSSBuilder().apply {
                                        display = Display.flex
                                        flex(1.0)
                                        flexDirection = FlexDirection.column
                                        marginTop = (-4).px
                                        marginLeft = 8.px
                                    }.toString()

                                    textFormatted(extraProps = mapOf("id" to "track"), weight = FontWeights.BOLD) {
                                        +(currentSong.item?.name ?: "")
                                    }
                                    textFormatted(
                                        extraProps = mapOf("id" to "artist"),
                                        color = if (currentSong.item != null) TextColors.DEFAULT else TextColors.GRAY,
                                    ) {
                                        +(currentSong
                                            .item
                                            ?.artists
                                            ?.joinToString(separator = ", ", transform = Artist::name)
                                            ?: "Nothing playing...")
                                    }
                                    currentSong.item?.let { _ ->
                                        div(classes = "progress-bar") {
                                            div(classes = if (!currentSong.playing) "paused" else "") {
                                                id = "progress"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    call.response.header(HttpHeaders.CacheControl, "s-maxage=1, stale-while-revalidate")
                    call.respondText(contentType = ContentType.Image.SVG, status = HttpStatusCode.OK) { body }
                }
            }
        }
    }
}