package com.hadar.auth.util.errors

import io.ktor.http.HttpStatusCode

class ValidateException(val exMassage: String, val code: Int = HttpStatusCode.Companion.BadRequest.value) : Exception(exMassage)