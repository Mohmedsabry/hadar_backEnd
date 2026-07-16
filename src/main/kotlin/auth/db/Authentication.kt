package com.hadar.auth.db

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

class Authentication : Table<Nothing>("authentication") {
    val uuid = varchar("uuid").primaryKey()
    val userId = varchar("user_id")
    val accessToken = varchar("access_token")
    val refreshToken = varchar("refresh_token")
}