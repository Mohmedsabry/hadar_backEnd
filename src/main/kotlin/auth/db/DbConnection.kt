package com.hadar.auth.db

import org.ktorm.database.Database

interface DbConnection {
    fun connect(): Database
}

class DbConnectionImp : DbConnection {
    override fun connect(): Database {
        return Database.connect(
            url = "jdbc:mysql://localhost:3306/hadar_db",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root"
        )
    }

}