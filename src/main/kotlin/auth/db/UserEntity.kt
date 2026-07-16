package com.hadar.auth.db

import org.ktorm.schema.Table
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar

class UserEntity : Table<Nothing>("user") {
    val uuid = varchar("uuid").primaryKey()
    val email = varchar("email")
    val phone = varchar("phone")
    val password = varchar("password")
    val role = varchar("role")
    val fName = varchar("first_name")
    val lName = varchar("last_name")
    val createdAt = timestamp("created_at")
}