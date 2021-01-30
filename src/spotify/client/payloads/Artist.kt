package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val name: String,
    val id: String,
)