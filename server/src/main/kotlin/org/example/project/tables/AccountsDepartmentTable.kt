package org.example.project.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object AccountsDepartmentTable: Table("accounts_department") {
    val id = integer("id").autoIncrement()
    val accountsName = varchar("accounts_name", 30)
    val createDate = timestamp("create_date")
    val authorLogin = varchar("author_login",30)
    val employeesNumber = integer("employees_number")
    override val primaryKey = PrimaryKey(id)
}