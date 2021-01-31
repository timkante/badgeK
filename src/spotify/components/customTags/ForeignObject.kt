package dev.timkante.badgeK.spotify.components.customTags

import kotlinx.html.*

class ForeignObject(
    consumer: TagConsumer<*>,
) : HTMLTag(
    tagName = "foreignObject",
    consumer = consumer,
    initialAttributes = emptyMap(),
    inlineTag = true,
    emptyTag = false,
), HtmlBlockTag {}

fun SVG.foreignObject(block: ForeignObject.() -> Unit = {}) {
    ForeignObject(consumer).visit(block)
}
