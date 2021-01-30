package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.Serializable

@Serializable
class ExternalId(
    val isrc: String? = null,
    val ean: String? = null,
    val upc: String? = null,
)