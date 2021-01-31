package dev.timkante.badgeK.spotify.components

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
}