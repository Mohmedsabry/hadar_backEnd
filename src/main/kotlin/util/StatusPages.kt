package com.hadar.util

import com.hadar.auth.util.errors.TokenExpiredException
import com.hadar.auth.util.errors.ValidateException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = Error(errorMessage = cause.message, statuesCode = HttpStatusCode.InternalServerError.value)
            )
        }
        exception<ValidateException> { call, massage ->
            call.respond(
                status = HttpStatusCode.fromValue(massage.code),
                message = Error(errorMessage = massage.exMassage, statuesCode = massage.code)
            )
        }
        exception<TokenExpiredException> { call, cause ->
            call.respond(
                status = HttpStatusCode.fromValue(cause.code),
                message = Error(errorMessage = cause.exMassage, statuesCode = cause.code)
            )
        }
    }
}
