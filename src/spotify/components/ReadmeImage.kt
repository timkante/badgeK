package dev.timkante.badgeK.spotify.components

import dev.timkante.badgeK.spotify.client.payloads.CurrentlyPlayingResponse
import dev.timkante.badgeK.spotify.components.customTags.foreignObject
import dev.timkante.badgeK.spotify.components.customTags.xhtmlNamespacedDiv
import dev.timkante.badgeK.styleCss
import io.ktor.html.*
import kotlinx.css.BoxSizing
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.html.*
import kotlinx.html.stream.createHTML

data class ReadmeImage(
    val width: Int,
    val height: Int,
) {
    val children = Placeholder<HtmlBlockTag>()
    val style = Placeholder<HtmlBlockTag>()

    operator fun invoke(block: ReadmeImage.() -> Unit): String = apply(block).run {
        createHTML(prettyPrint = true, xhtmlCompatible = true).svg {
            mapOf(
                "fill" to "none",
                "width" to "$width",
                "height" to "$height",
                "viewBox" to "0 0 $width $height",
            ).let(attributes::putAll)

            foreignObject {
                mapOf(
                    "width" to "$width",
                    "height" to "$height",
                ).let(attributes::putAll)

                xhtmlNamespacedDiv {
                    styleCss {
                        rule("*") {
                            margin(0.px)
                            boxSizing = BoxSizing.borderBox
                        }
                    }
                    insert(this@run.style)
                    insert(children)
                }
            }
        }
    }

    // language=scss
    fun mainCssFor(forResponse: CurrentlyPlayingResponse): String = """
            |.paused { 
            |  animation-play-state: paused !important;
            |  background: #e1e4e8 !important;
            |}
|
            |img:not([src]) {
            |  content: url("data:image/gif;base64,R0lGODlhAQABAPAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==");
            |  background: #FFF;
            |  border: 1px solid #e1e4e8;
            |}
|
            |img {
            |  border-radius: 3px;
            |}
|
            |p {
            |  display: block;
            |  opacity: 0;
            |}
|
            |.progress-bar {
            |  position: relative;
            |  width: 100%;
            |  max-width: 360px;
            |  height: 4px;
            |  margin: -1px;
            |  border: 1px solid #e1e4e8;
            |  border-radius: 4px;
            |  overflow: hidden;
            |  padding: 2px;
            |  z-index: 0;
            |}
|
            |#progress {
            |  position: absolute;
            |  top: -1px;
            |  left: 0;
            |  width: 100%;
            |  height: 6px;
            |  transform-origin: left center;
            |  background-color: #24292e;
            |  animation: progress ${forResponse.item?.durationInMillis ?: 0}ms linear;
            |  animation-delay: -${forResponse.progressInMillis}ms;
            |}
            |
            |.progress-bar,
            |#track,
            |#artist,
            |#cover {
            |  opacity: 0;
            |  animation: appear 300ms ease-out forwards;
            |}
|
            |#track {
            |  animation-delay: 400ms;
            |}
            |#artist {
            |  animation-delay: 500ms;
            |}
            |.progress-bar {
            |  animation-delay: 550ms;
            |  margin-top: 4px;
            |}
|
            |#cover {
            |  animation-name: cover-appear;
            |  animation-delay: 300ms;
            |  box-shadow: 0 1px 3px rgba(0,0,0,0.1), 0 3px 10px rgba(0,0,0,0.05);
            |}
|
            |#cover:not([src]) {
            |  box-shadow: none;
            |}
|
            |@keyframes cover-appear {
            |  from {
            |    opacity: 0;
            |    transform: scale(0.8);
            |  }
            |  to {
            |    opacity: 1;
            |    transform: scale(1);
            |  }
            |}
|
            |@keyframes appear {
            |  from {
            |    opacity: 0;
            |    transform: translateX(-8px);
            |  }
            |  to {
            |    opacity: 1;
            |    transform: translateX(0);
            |  }
            |}
|
            |@keyframes progress {
            |  from {
            |    transform: scaleX(0)
            |  }
            |  to {
            |    transform: scaleX(1)
            |  }
            |}
|
            |[data-color-mode=auto][data-dark-theme=dark] {
            |  #cover {
            |    box-shadow: 0 1px 3px rgb(255,255,255,0.1), 0 3px 7px rgb(255,255,255,0.05);
            |  }
            |  #progress {
            |    background-color: #e1e4e8;
            |  }
            |  .progress-bar {
            |    border: 1px solid #555f69;
            |  }
            |  #track {
            |    color: #e1e4e8 !important;
            |  }
            |  #artist {
            |    color: #555f69 !important;
            |  }
            |}
            |""".trimMargin(marginPrefix = "|")
}