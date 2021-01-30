package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val album: Album,
    @SerialName("external_ids") val externalIds: ExternalId,
    val popularity: Long,
    @SerialName("is_local") val isLocal: Boolean? = null,
)
