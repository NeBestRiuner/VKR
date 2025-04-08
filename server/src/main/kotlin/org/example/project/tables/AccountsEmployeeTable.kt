package org.example.project.tables

import org.example.project.tables.UserTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object AccountsEmployeeTable: Table("accounting_employee") {
    val employeeId = integer("employee_id").autoIncrement()
    val departmentId = integer("department_id")
    val userId = integer("user_id")
    val dateOfEmployment = timestamp("date_of_employment")
    val dateDismissal = timestamp("date_dismissal").nullable()
    init {
        foreignKey(departmentId to AccountsDepartmentTable.id)
        foreignKey(userId to UserTable.id)
    }
    override val primaryKey = PrimaryKey(employeeId)
}