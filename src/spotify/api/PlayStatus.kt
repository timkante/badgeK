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
import kotlinx.css.properties.Angle
import kotlinx.css.properties.Transform
import kotlinx.css.properties.Transforms
import kotlinx.css.properties.rotate
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.Collections.rotate
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
                    val spotifyLogo = "data:image/png;base64,${
                        Base64.getEncoder().encodeToString(
                            client.fetchImage("https://1000logos.net/wp-content/uploads/2017/08/Spotify-Logo.png")
                        )
                    }"
                    val body = (ReadmeImage(width = 540, height = 92)) {
                        style {
                            style {
                                +mainCssFor(currentSong)
                            }
                        }
                        children {
                            div {
                                id = "spotify-logo"
                                attributes += "style" to CSSBuilder().apply {
                                    background = "white"
                                    backgroundImage = Image("url($spotifyLogo)")
                                    backgroundPosition = "center center"
                                    backgroundSize = "cover"
                                    zIndex = 2
                                    position = Position.absolute
                                    borderRadius = 50.pct
                                    width = 32.px
                                    height = 32.px
                                    transform = Transforms().apply { rotate(Angle("-30deg")) }
                                }.toString()
                            }

                            div {
                                attributes += "style" to CSSBuilder().apply {
                                    background = "#00d76c"
                                    position = Position.absolute
                                    width = 155.px
                                    height = 20.px
                                    left = 28.px
                                    top = 6.px
                                    zIndex = 1
                                    fontSize = 14.px
                                    textAlign = TextAlign.center
                                    paddingTop = 1.px
                                    borderTopRightRadius = 10.px
                                    borderBottomRightRadius = 10.px
                                    fontWeight = FontWeight.w400
                                    color = Color.white
                                }.toString()
                                +"Now playing on Spotify"
                            }

                            div(classes = if (currentSong.playing) "disabled" else "") {
                                attributes += "style" to CSSBuilder().apply {
                                    display = Display.flex
                                    alignItems = Align.center
                                    paddingTop = 8.px
                                    paddingRight = 10.px
                                    paddingLeft = 4.px
                                    marginTop = 16.px
                                    marginLeft = 16.px
                                    position = Position.absolute
                                    top = 0.px
                                    left = 0.px
                                    background = "#00a4fd"
                                    borderTopRightRadius = 30.px
                                    borderBottomLeftRadius = 30.px
                                }.toString()

                                img(src = if (imageData != null) coverImage else null) {
                                    width = "48"
                                    height = "48"
                                    id = "cover"
                                    attributes += "style" to CSSBuilder().apply {
                                        marginLeft = 14.px
                                        marginTop = 8.px
                                        marginBottom = 10.px
                                    }.toString()
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