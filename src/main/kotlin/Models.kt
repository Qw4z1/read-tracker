package io.nyblom

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Serializable
data class IncrementRead(val slug: String)

@Serializable
data class CreateRead(val slug: String, val name: String)

@Serializable
data class FullRead(val slug: String, val name: String, val reads: Int)

object Reads : Table() {
    val slug: Column<String> = varchar("slug", 50)
    val name: Column<String> = varchar("name", 50)
    val reads: Column<Int> = integer("reads")
    override val primaryKey = PrimaryKey(slug)
}