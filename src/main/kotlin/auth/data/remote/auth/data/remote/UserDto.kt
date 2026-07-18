package com.hadar.auth.data.remote.auth.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val phone: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    /** ADMIN , USER , OWNER , DELIVERY*/
    val role: String? = "USER",
)
