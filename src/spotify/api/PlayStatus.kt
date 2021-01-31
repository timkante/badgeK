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
                                +"""
            .paused { 
              animation-play-state: paused !important;
              background: #e1e4e8 !important;
            }

            img:not([src]) {
              content: url("data:image/gif;base64,R0lGODlhAQABAPAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==");
              background: #FFF;
              border: 1px solid #e1e4e8;
            }

            img {
              border-radius: 3px;
            }

            p {
              display: block;
              opacity: 0;
            }

            .progress-bar {
              position: relative;
              width: 100%;
              max-width: 360px;
              height: 4px;
              margin: -1px;
              border: 1px solid #e1e4e8;
              border-radius: 4px;
              overflow: hidden;
              padding: 2px;
              z-index: 0;
            }

            #progress {
              position: absolute;
              top: -1px;
              left: 0;
              width: 100%;
              height: 6px;
              transform-origin: left center;
              background-color: #24292e;
              animation: progress ${currentSong.item?.durationInMillis ?: 0}ms linear;
              animation-delay: -${currentSong.progressInMillis}ms;
            }
            
            .progress-bar,
            #track,
            #artist,
            #cover {
              opacity: 0;
              animation: appear 300ms ease-out forwards;
            }

            #track {
              animation-delay: 400ms;
            }
            #artist {
              animation-delay: 500ms;
            }
            .progress-bar {
              animation-delay: 550ms;
              margin-top: 4px;
            }

            #cover {
              animation-name: cover-appear;
              animation-delay: 300ms;
              box-shadow: 0 1px 3px rgba(0,0,0,0.1), 0 3px 10px rgba(0,0,0,0.05);
            }

            #cover:not([src]) {
              box-shadow: none;
            }

            @keyframes cover-appear {
              from {
                opacity: 0;
                transform: scale(0.8);
              }
              to {
                opacity: 1;
                transform: scale(1);
              }
            }

            @keyframes appear {
              from {
                opacity: 0;
                transform: translateX(-8px);
              }
              to {
                opacity: 1;
                transform: translateX(0);
              }
            }

            @keyframes progress {
              from {
                transform: scaleX(0)
              }
              to {
                transform: scaleX(1)
              }
            }
        
                                """.trimIndent()
                            }
                        }
                        children {
                            div(classes = if (currentSong.playing) "disabled" else "") {
                                attributes += "style" to CSSBuilder().apply{
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
                                    currentSong.item?.let { track ->
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