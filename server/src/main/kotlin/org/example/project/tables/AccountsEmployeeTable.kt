package org.example.project.tables

import org.example.project.tables.UserTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object AccountsEmployeeTable: IdTable<Int>("accounting_employee") {
    override val id: Column<EntityID<Int>> = integer("employee_id").autoIncrement().entityId()
    val departmentId = integer("department_id")
    val userId = integer("user_id")
    val dateOfEmployment = timestamp("date_of_employment")
    val dateDismissal = timestamp("date_dismissal").nullable()
    init {
        foreignKey(departmentId to AccountsDepartmentTable.id)
        foreignKey(userId to UserTable.id)
    }
    override val primaryKey = PrimaryKey(id)
}