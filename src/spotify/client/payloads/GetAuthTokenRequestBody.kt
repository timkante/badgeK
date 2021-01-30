package dev.timkante.badgeK.spotify.client.payloads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAuthTokenRequestBody(
    @SerialName("grant_type") val grantType: String = "refresh_token",
    @SerialName("refresh_token") val refreshToken: String,
) {
    val formUrlEncoded: String get() = "grant_type=$grantType&refresh_token=$refreshToken"
}