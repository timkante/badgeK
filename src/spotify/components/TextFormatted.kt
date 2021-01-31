package dev.timkante.badgeK.spotify.components

import dev.timkante.badgeK.spotify.components.styles.FontFamilies
import dev.timkante.badgeK.spotify.components.styles.FontWeights
import dev.timkante.badgeK.spotify.components.styles.TextColors
import dev.timkante.badgeK.spotify.components.styles.TextSize
import dev.timkante.badgeK.styleCss
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.FontWeight
import kotlinx.css.WhiteSpace
import kotlinx.css.properties.LineHeight
import kotlinx.html.*

@HtmlTagMarker
fun FlowContent.textFormatted(
    weight: FontWeights = FontWeights.DEFAULT,
    family: FontFamilies = FontFamilies.DEFAULT,
    color: TextColors = TextColors.DEFAULT,
    size: TextSize = TextSize.DEFAULT,
    extraProps: Map<String, String> = emptyMap(),
    extraStyles: CSSBuilder.() -> Unit = {},
    children: P.() -> Unit = {},
): Unit = p {
    (extraProps + ("style" to CSSBuilder().apply {
        whiteSpace = WhiteSpace.pre
        fontSize = size.size
        lineHeight = LineHeight("1.5")
        fontFamily = family.fonts
        this.color = Color(color.hex)
        fontWeight = FontWeight("${weight.weight}")
        extraStyles()
    }.toString())).let(attributes::putAll)

    children()
}