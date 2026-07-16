package com.hadar.auth.routing

import com.hadar.auth.data.remote.auth.data.remote.ReceiveUserDto
import com.hadar.auth.domain.repository.AuthRepository
import com.hadar.auth.domain.util.TokenType
import com.hadar.auth.util.errors.TokenExpiredException
import com.hadar.auth.util.errors.ValidateException
import com.hadar.util.Success
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRouting(
    authRepository: AuthRepository,
    config: ApplicationConfig
) {
    routing {
        post("/register") {
            val user = call.receive<ReceiveUserDto>()
            authRepository.register(user, config)
            call.respond(HttpStatusCode.OK, Success(data = "user inserted successfully"))
        }
        get("/login") {
            val email = call.request.queryParameters["email"] ?: throw ValidateException(
                "there is no email or phone sent",
                HttpStatusCode.NotFound.value
            )
            val password = call.request.queryParameters["password"] ?: throw ValidateException(
                "there is no password sent",
                HttpStatusCode.NotFound.value
            )
            val token = authRepository.login(
                email = email,
                password = password,
                config = config
            )
            call.response.headers.append("access-token", token)
            call.respond(Success(statuesCode = HttpStatusCode.OK.value, data = token))
        }
        authenticate("auth-jwt") {
            get("/user") {
                val token =
                    call.request.headers["Authorization"]?.split(' ')?.lastOrNull() ?: throw TokenExpiredException(
                        exMassage = "Token Not Set",
                        code = HttpStatusCode.NonAuthoritativeInformation.value
                    )
                println("token $token")
                val userId = authRepository.validateUserToken(
                    token = token,
                    tokenType = TokenType.ACCESS
                ) ?: throw TokenExpiredException()
                println("userId $userId")
                val user = authRepository.getUser(userId)
                call.respond(Success(data = user))
            }
            post("/refreshAccessToken") {
                val token =
                    call.request.headers["Authorization"]?.split(' ')?.lastOrNull() ?: throw TokenExpiredException(
                        exMassage = "Token Not Set",
                        code = HttpStatusCode.NonAuthoritativeInformation.value
                    )
                println("token $token")
                val userId = authRepository.validateUserToken(
                    token = token,
                    tokenType = TokenType.REFRESH
                ) ?: throw TokenExpiredException()
                println("userId $userId")
                val newAccessToken = authRepository.refreshAccessToken(
                    userId = userId,
                    config = config
                )
                call.response.headers.append("access-token", newAccessToken)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = Success(data = newAccessToken)
                )
            }
        }
    }
}