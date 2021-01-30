package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentlyPlayingResponse(
    val timestamp: Long,
    @SerialName("progress_ms") val progressInMillis: Long? = null,
    @SerialName("is_playing") val playing: Boolean,
    val item: Track? = null,
)