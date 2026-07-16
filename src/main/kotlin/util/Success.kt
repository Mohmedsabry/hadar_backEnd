package com.hadar.util

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Success<T>(
    val statuesCode: Int = HttpStatusCode.OK.value,
    val data: T
)
