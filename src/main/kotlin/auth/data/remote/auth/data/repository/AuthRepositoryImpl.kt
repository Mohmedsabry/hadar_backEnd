package com.hadar.auth.data.remote.auth.data.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hadar.auth.data.remote.auth.data.remote.ReceiveUserDto
import com.hadar.auth.data.remote.auth.data.remote.UserDto
import com.hadar.auth.db.Authentication
import com.hadar.auth.db.DbConnection
import com.hadar.auth.db.UserEntity
import com.hadar.auth.domain.model.User
import com.hadar.auth.domain.repository.AuthRepository
import com.hadar.auth.domain.util.TokenType
import com.hadar.auth.util.AuthValidation
import com.hadar.auth.util.errors.ValidateException
import io.ktor.http.*
import io.ktor.server.config.*
import org.bson.types.ObjectId
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthRepositoryImpl(
    private val userEntity: UserEntity,
    private val dbConnection: DbConnection,
    private val authValidation: AuthValidation,
    private val authentication: Authentication
) : AuthRepository {
    private val connection by lazy {
        dbConnection.connect()
    }
    private val accessTokenDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
    private val refreshTokenDate = System.currentTimeMillis() + 60 * 24 * 60 * 60 * 1000
    private fun generateToken(
        config: ApplicationConfig,
        expireDate: Date
    ): String {
        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()
        val audience = config.property("jwt.audience").getString()
        // working for 2 months
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(expireDate)
            .sign(Algorithm.HMAC256(secret))
        return token
    }

    override suspend fun register(
        user: ReceiveUserDto,
        config: ApplicationConfig
    ) {
        if (user.email == null) return
        if (user.password == null) return
        if (user.firstName == null) return
        if (user.lastName == null) return
        authValidation.validate(
            email = user.email,
            password = user.password,
            phone = user.phone.orEmpty(),
            fName = user.firstName,
            lName = user.lastName
        )
        val userId = ObjectId.get().toString()
        connection.insert(userEntity) {
            set(userEntity.email, user.email)
            set(userEntity.phone, user.phone)
            set(userEntity.role, user.role)
            set(userEntity.fName, user.firstName)
            set(userEntity.lName, user.lastName)
            set(userEntity.uuid, userId)
            set(userEntity.password, BCrypt.hashpw(user.password, BCrypt.gensalt()))
        }
        // working for 2 months
        val refreshToken = generateToken(
            config = config,
            expireDate = Date(refreshTokenDate)
        )
        connection.insert(
            authentication
        ) {
            set(authentication.uuid, ObjectId.get().toString())
            set(authentication.refreshToken, refreshToken)
            set(authentication.userId, userId)
        }
    }

    override suspend fun login(
        email: String,
        password: String,
        config: ApplicationConfig
    ): Map<TokenType, String> {
        val user = connection.from(userEntity)
            .select()
            .where {
                ((userEntity.email eq email) or (userEntity.phone eq email))
            }.map {
                User(
                    phone = it[userEntity.phone].orEmpty(),
                    email = it[userEntity.email].orEmpty(),
                    firstName = it[userEntity.fName].orEmpty(),
                    lastName = it[userEntity.lName].orEmpty(),
                    password = it[userEntity.password].orEmpty(),
                    id = it[userEntity.uuid].orEmpty()
                )
            }.firstOrNull()
        println(user)
        if (user == null) {
            throw ValidateException("there is unCorrect email or password", HttpStatusCode.Companion.Unauthorized.value)
        }
        println(!BCrypt.checkpw(password, user.password))
        if (!BCrypt.checkpw(password, user.password)) {
            throw ValidateException("there is unCorrect email or password", HttpStatusCode.Companion.Unauthorized.value)
        }
        val accessToken = generateToken(
            config = config,
            expireDate = Date(accessTokenDate)
        )
        val refreshToken = generateToken(
            config = config,
            expireDate = Date(refreshTokenDate)
        )
        connection.update(
            authentication
        ) {
            set(authentication.accessToken, accessToken)
            set(authentication.refreshToken, refreshToken)
            where {
                authentication.userId eq user.id
            }
        }
        return mapOf(TokenType.ACCESS to accessToken, TokenType.REFRESH to refreshToken)
    }

    override suspend fun getUser(
        userId: String
    ): UserDto? {
        return connection.from(userEntity)
            .select()
            .where {
                userEntity.uuid eq userId
            }.map { query ->
                UserDto(
                    email = query[userEntity.email],
                    phone = query[userEntity.phone],
                    firstName = query[userEntity.fName],
                    lastName = query[userEntity.lName],
                    role = query[userEntity.role]
                )
            }.firstOrNull()
    }

    override suspend fun validateUserToken(
        token: String,
        tokenType: TokenType
    ): String? {
        var userId: String?
        when (tokenType) {
            TokenType.ACCESS -> {
                userId = connection.from(authentication).select()
                    .where {
                        authentication.accessToken eq token
                    }.map {
                        it[authentication.userId]
                    }.firstOrNull()
            }

            TokenType.REFRESH -> {
                userId = connection.from(authentication).select()
                    .where {
                        authentication.refreshToken eq token
                    }.map {
                        it[authentication.userId]
                    }.firstOrNull()
            }
        }
        return userId
    }

    override suspend fun refreshAccessToken(
        userId: String,
        config: ApplicationConfig
    ): String {
        val token = generateToken(
            config = config,
            expireDate = Date(accessTokenDate)
        )
        connection.update(authentication) {
            set(authentication.accessToken, token)
            where {
                authentication.userId eq userId
            }
        }
        return token
    }
}