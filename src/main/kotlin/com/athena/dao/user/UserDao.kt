package com.athena.dao.user

import com.athena.model.SignUpParams
import com.athena.model.User

interface UserDao {
    suspend fun insert(params: SignUpParams): User?
    suspend fun findByEmail(email: String): User?
}