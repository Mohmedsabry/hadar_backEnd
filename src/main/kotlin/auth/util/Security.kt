package com.hadar.auth.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*

fun Application.configureSecurity(
    config: ApplicationConfig
) {
    install(Authentication) {
        jwt("auth-jwt") {
            val secret = config.property("jwt.secret").getString()
            val issuer = config.property("jwt.issuer").getString()
            val audience = config.property("jwt.audience").getString()
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            }
            validate {  }
        }
    }
}
/**
 * validate { crd ->
 *                 crd.
 *                 val userId = crd.getClaim("user_id", String::class)
 *                 if (userId?.isEmpty() == false) {
 *                     JWTPrincipal(crd.payload)
 *                 } else {
 *                     null
 *                 }
 *             }
 * */