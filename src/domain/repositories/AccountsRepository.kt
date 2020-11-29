package com.gymfit.backend.domain.repositories

import io.ktor.util.KtorExperimentalAPI

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

import com.gymfit.backend.domain.entities.Account
import com.gymfit.backend.domain.entities.AccountsTable

import com.gymfit.backend.infrastructure.database.DatabaseFactory

import java.util.UUID

@KtorExperimentalAPI
class AccountsRepository(val database: DatabaseFactory, val table: AccountsTable) {
    suspend fun findById(id: UUID): Account? = database.query {
        table.select { table.id eq id }.mapNotNull { toAccount(it) }.singleOrNull()
    }

    private fun toAccount(row: ResultRow): Account = Account(
        id = row[table.id],
    )
}