package com.gymfit.backend.domain.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object AccountsTable: Table("accounts") {
    val id: Column<UUID> = uuid("id").primaryKey()
    val name: Column<String> = varchar("name", 256)
}

data class Account(
    val id: UUID
)
