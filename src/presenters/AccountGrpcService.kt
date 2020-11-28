package com.gymfit.backend.presenters

import generated.proto.AccountServiceGrpcKt
import generated.proto.GetAccountInfoRequest
import generated.proto.GetAccountInfoResponse

class AccountGrpcService: AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {
    override suspend fun getAccountInfo(request: GetAccountInfoRequest): GetAccountInfoResponse {
        val response = GetAccountInfoResponse.newBuilder()
        return response.build()
    }
}