package com.athena.repo

import com.athena.model.AuthResponse
import com.athena.model.SignInParams
import com.athena.model.SignUpParams
import com.athena.util.Response

interface UserRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}