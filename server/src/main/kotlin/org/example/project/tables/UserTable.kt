package org.example.project.tables
import org.jetbrains.exposed.sql.Table


object UserTable : Table("user_table") {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 30)
    val password = varchar("password",30)
    val firstname = varchar("firstname",30)
    val lastname = varchar("lastname",30)
    val patronymic = varchar("patronymic",30)
    val phone = varchar("phone",12)

    override val primaryKey = PrimaryKey(id)
}