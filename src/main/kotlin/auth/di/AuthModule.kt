package com.hadar.auth.di

import com.hadar.auth.data.remote.auth.data.repository.AuthRepositoryImpl
import com.hadar.auth.db.Authentication
import com.hadar.auth.db.DbConnection
import com.hadar.auth.db.DbConnectionImp
import com.hadar.auth.db.UserEntity
import com.hadar.auth.domain.repository.AuthRepository
import com.hadar.auth.util.AuthValidation
import org.koin.dsl.module

val authModule = module {
    single {
        UserEntity()
    }
    single<DbConnection> {
        DbConnectionImp()
    }
    single {
        AuthValidation()
    }
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), get(), get())
    }
    single {
        Authentication()
    }
}