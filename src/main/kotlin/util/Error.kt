package com.hadar.util

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val statuesCode: Int = HttpStatusCode.BadRequest.value,
    val errorMessage: String? = null
)
