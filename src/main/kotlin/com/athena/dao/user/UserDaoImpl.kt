package com.athena.dao.user

import com.athena.dao.DatabaseFactory.dbQuery
import com.athena.model.SignUpParams
import com.athena.model.User
import com.athena.model.UserRow
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): User? {
       return dbQuery{
            val insertSatement = UserRow.insert {
                it[name] = params.name
                it[email] = params.email
                it[password] = params.password
            }

            insertSatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return  dbQuery {
            UserRow.select { UserRow.email eq email }
                .map{ rowToUser(it)}
            .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow): User{
        return User(
            id = row[UserRow.id],
            name = row[UserRow.name],
            bio = row[UserRow.bio],
            avatar = row[UserRow.avatar],
            password = row[UserRow.password]
        )
    }
}