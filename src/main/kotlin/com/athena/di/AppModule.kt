package com.athena.di

import com.athena.dao.user.UserDao
import com.athena.dao.user.UserDaoImpl
import com.athena.repo.UserRepository
import com.athena.repo.UserRepositoryImpl
import org.koin.dsl.module

var appModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}