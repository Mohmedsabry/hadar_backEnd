package com.hadar

import com.hadar.auth.di.authModule
import com.hadar.auth.routing.authRouting
import com.hadar.auth.util.configureSecurity
import com.hadar.util.configureStatusPages
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin


fun main(array: Array<String>) {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
        watchPaths = array.toList()
    ).start(wait = true)
}

fun Application.module() {
    val config = HoconApplicationConfig(
        ConfigFactory.load("application.conf")
    )
    configureKoin()
    configureRateLimiting()
    configureSerialization()
    configureSse()
    configureStatusPages()
    configureWebsockets()
    configureSecurity(config)
    authRouting(get(), config)
}

fun Application.configureKoin() {
    install(Koin) {
        modules(authModule)
    }
}
