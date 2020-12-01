package com.gymfit.backend.domain.repositories

import io.ktor.util.KtorExperimentalAPI

import org.jetbrains.exposed.sql.select

import com.gymfit.backend.domain.entities.AccountsTable
import com.gymfit.backend.domain.entities.AccountEntity
import com.gymfit.backend.domain.entities.AccountModel

import com.gymfit.backend.infrastructure.database.DatabaseFactory

import java.util.UUID

@KtorExperimentalAPI
class AccountsRepository(val database: DatabaseFactory) {
    suspend fun findById(id: UUID): AccountModel? = database.query {
        AccountsTable.select { AccountsTable.id eq id }.map {
            AccountEntity.wrapRow(it).toDomain()
        }.firstOrNull()
    }

}