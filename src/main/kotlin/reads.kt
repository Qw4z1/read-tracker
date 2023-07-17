package io.nyblom

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Route.trackingRouting() {
    route(path = "/reads") {
        post {
            val read = call.receive<CreateRead>()
            Reads.insertIgnore {
                it[reads] = 0
                it[name] = read.name
                it[slug] = read.slug
            }
            call.respondText { "${read.slug} successfully created" }
        }
        patch(path = "{slug}") {
            val slug = call.parameters["slug"].toString()
            val updated = transaction {
                Reads.update({ Reads.slug eq slug }) {
                    it[reads] = reads + 1
                }
                val reads = Reads.select { Reads.slug eq slug }
                val read = reads.first()
                FullRead(
                    slug = read[Reads.slug],
                    name = read[Reads.name],
                    reads = read[Reads.reads]
                )
            }
            call.respond(updated)
        }
        get(path = "{slug}") {
            val slug = call.parameters["slug"].toString()
            val reads = transaction {
                val reads = Reads.select { Reads.slug eq slug }
                val read = reads.first()
                FullRead(
                    slug = read[Reads.slug],
                    name = read[Reads.name],
                    reads = read[Reads.reads]
                )
            }
            call.respond(reads)
        }
    }
}
