package com.hadar

import io.ktor.server.application.*
import io.ktor.server.sse.*

fun Application.configureSse() {
    install(SSE)
}
