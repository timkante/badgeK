package dev.timkante.badgeK.spotify.components.customTags

import kotlinx.html.*

class XhtmlNamespacedDiv(
    consumer: TagConsumer<*>,
) : HTMLTag(
    tagName = "div",
    consumer = consumer,
    initialAttributes = emptyMap(),
    namespace = "http://www.w3.org/1999/xhtml",
    inlineTag = true,
    emptyTag = false,
), HtmlBlockTag {}

fun ForeignObject.xhtmlNamespacedDiv(block: XhtmlNamespacedDiv.() -> Unit = {}) {
    XhtmlNamespacedDiv(consumer).visit(block)
}
