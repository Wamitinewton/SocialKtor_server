package com.athena.repository.user

import com.athena.dao.user.UserDao
import com.athena.model.AuthResponse
import com.athena.model.AuthResponseData
import com.athena.model.SignInParams
import com.athena.model.SignUpParams
import com.athena.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return  if (userAlreadyExists(params.email)){
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "A user with this email already exists"
                )
            )
        } else{
            val insertedUser = userDao.insert(params)

            if (insertedUser == null){
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Oops, sorry we could not register user, try later!"
                    )
                )
            } else{
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            bio = insertedUser.bio,
                            token = "Here is you token",

                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        TODO("Not yet implemented")
    }

    private suspend fun userAlreadyExists(email:String): Boolean{
        return userDao.findByEmail(email) !=null
    }
}