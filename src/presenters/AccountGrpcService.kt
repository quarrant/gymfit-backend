package com.gymfit.backend.presenters

import com.gymfit.backend.domain.repositories.AccountsRepository
import generated.proto.Account
import generated.proto.AccountServiceGrpcKt
import generated.proto.GetAccountRequest
import generated.proto.GetAccountResponse
import io.ktor.util.*
import java.util.*

@KtorExperimentalAPI
class AccountGrpcService(private val accountsRepository: AccountsRepository) :
    AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {
    override suspend fun getAccount(request: GetAccountRequest): GetAccountResponse {
        val result =
            accountsRepository.findById(UUID.fromString(request.id)) ?: throw Error("ACCOUNT_NOT_FOUND")

        val response = GetAccountResponse.newBuilder().apply {
            account = Account.newBuilder().apply {
                id = result.id.toString()
            }.build()
        }

        return response.build()
    }
}