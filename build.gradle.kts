
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.hadar"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("com.hadar.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}
ktor{
    development = true
}
kotlin {
    jvmToolchain(17)
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
    implementation("io.insert-koin:koin-ktor:4.1.1")
    // ktorm DB
    implementation("org.ktorm:ktorm-core:4.0.0")
    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("org.mindrot:jbcrypt:0.4")
    // jwt
    implementation("io.ktor:ktor-server-auth:3.0.0-rc-2")
    implementation("io.ktor:ktor-server-auth-jwt:3.0.0-rc-2")
}
