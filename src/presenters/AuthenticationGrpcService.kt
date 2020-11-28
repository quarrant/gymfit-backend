package com.gymfit.backend.presenters

import generated.proto.AuthenticationServiceGrpcKt
import generated.proto.SignInAnonymousRequest
import generated.proto.SignInAnonymousResponse

class AuthenticationGrpcService: AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineImplBase() {
    override suspend fun signInAnonymous(request: SignInAnonymousRequest): SignInAnonymousResponse {
        val response = SignInAnonymousResponse.newBuilder()
        return response.build()
    }
}