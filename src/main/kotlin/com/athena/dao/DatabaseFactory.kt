package com.athena.dao

import com.athena.model.UserRow
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(){
        Database.connect(createHikariDatasource())
        transaction {
            SchemaUtils.create(UserRow)
        }
    }

    private fun createHikariDatasource(): HikariDataSource{
        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/socialmediadb"
        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            username = System.getenv("database.username")
            password = System.getenv("database.password")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()

        }
        return  HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: suspend () -> T)=
        newSuspendedTransaction(Dispatchers.IO) { block() }
}