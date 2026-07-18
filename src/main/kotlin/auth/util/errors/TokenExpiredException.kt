package com.hadar.auth.util.errors

import io.ktor.http.*

class TokenExpiredException(
    val code: Int = HttpStatusCode.Unauthorized.value,
     val exMassage: String? = "invalid Token"
) : Exception(exMassage)