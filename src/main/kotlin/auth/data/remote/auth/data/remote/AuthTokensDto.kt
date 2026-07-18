package com.hadar.auth.data.remote.auth.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokensDto(
    val accessToken: String?,
    val refreshToken: String?
)