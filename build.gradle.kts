
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.hadar"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.routingOpenapi)
    implementation(ktorLibs.server.sessions)
    implementation(ktorLibs.server.sse)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.server.websockets)
    implementation(libs.flaxoos.ktor.server.rateLimiting)
    implementation(libs.logback.classic)
    implementation(libs.mongodb.bson)
    implementation(libs.mongodb.driverCore)
    implementation(libs.mongodb.driverSync)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
