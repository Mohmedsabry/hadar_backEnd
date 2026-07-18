package com.hadar.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val phone: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    /** ADMIN , USER , OWNER , DELIVERY*/
    val role: String = "USER",
    val id: String = ""
)