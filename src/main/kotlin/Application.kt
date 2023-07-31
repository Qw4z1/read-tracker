package io.nyblom

import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = EngineMain.main(args)

// module() used in resources/application.conf
fun Application.module() {
    configureAuthentication()
    configureSerialization()
    configureRouting()
    connectDb()
    configureCors()
}

private fun Application.connectDb() {
    val host = environment.config.propertyOrNull("db.host")?.getString()!!
    val database = environment.config.propertyOrNull("db.database")?.getString()!!
    val user = environment.config.propertyOrNull("db.user")?.getString()!!
    val password = environment.config.propertyOrNull("db.password")?.getString()!!

    Database.connect(
        "jdbc:mysql://$host/$database", driver = "com.mysql.cj.jdbc.Driver",
        user = user, password = password
    )

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Reads)
    }
}

private fun Application.configureRouting() {
    install(IgnoreTrailingSlash)
    routing {
        authenticate("auth-bearer") {
            trackingRouting()
        }
    }
}

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

private fun Application.configureCors() {
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
    }
}

private fun Application.configureAuthentication() {
    val token = environment.config.propertyOrNull("auth.token")?.getString()!!

    install(Authentication) {
        bearer("auth-bearer") {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                if (tokenCredential.token == token) {
                    UserIdPrincipal("nyblomioweb")
                } else {
                    null
                }
            }
        }
    }
}