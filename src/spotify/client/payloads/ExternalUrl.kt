package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrl(
    val spotify: String
)