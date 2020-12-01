package com.gymfit.backend.domain.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable

import java.util.UUID

object AccountsTable: UUIDTable("accounts") {
    val name = varchar("name", 256)
}

class AccountEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<AccountEntity>(AccountsTable)

    var name by AccountsTable.name

    fun toDomain(): AccountDAO {
        return AccountDAO(id.value)
    }

}

data class AccountDAO(
    val id: UUID
)
