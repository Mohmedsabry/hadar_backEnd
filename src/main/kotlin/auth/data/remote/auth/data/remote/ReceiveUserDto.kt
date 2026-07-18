package com.hadar.auth.data.remote.auth.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ReceiveUserDto(
    val phone: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val password: String?,
    /** ADMIN , USER , OWNER , DELIVERY*/
    val role: String? = "USER",
)
