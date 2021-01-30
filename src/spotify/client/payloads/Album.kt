package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("album_group") val group: String? = null, /* "album" | "single" | "compilation" | "appears_on" */
    @SerialName("album_type") val type: String? = null, /* "album" | "single" | "compilation" */
    val artists: Array<Artist>,
    val id: String,
    val images: Array<Image>,
    val name: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String, /* "year" | "month" | "day" */
)
