package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val height: Int? = null,
    val url: String,
    val width: Int? = null,
)