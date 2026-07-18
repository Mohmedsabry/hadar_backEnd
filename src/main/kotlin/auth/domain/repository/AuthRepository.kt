package com.hadar.auth.domain.repository

import com.hadar.auth.data.remote.auth.data.remote.ReceiveUserDto
import com.hadar.auth.data.remote.auth.data.remote.UserDto
import com.hadar.auth.domain.util.TokenType
import io.ktor.server.config.*

interface AuthRepository {
    suspend fun register(
        user: ReceiveUserDto,
        config: ApplicationConfig
    )

    suspend fun login(
        email: String,
        password: String,
        config: ApplicationConfig
    ): Map<TokenType, String>

    suspend fun getUser(userId: String): UserDto?
    suspend fun validateUserToken(
        token: String,
        tokenType: TokenType
    ): String?

    suspend fun refreshAccessToken(
        userId: String,
        config: ApplicationConfig
    ): String
}