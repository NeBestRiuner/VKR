package org.example.project.tables

import org.example.project.tables.AccountsEmployeeTable.departmentId
import org.example.project.tables.AccountsEmployeeTable.userId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column


object EmployeePermissionTable: IdTable<Int>("employee_permission") {
    override val id : Column<EntityID<Int>> = integer("emp_per_id").autoIncrement().entityId()
    val employeeId = integer("employee_id")
    val permissionId = integer("permission_id")

    init {
        foreignKey(employeeId to AccountsEmployeeTable.id)
        foreignKey(permissionId to PermissionTable.permissionId)
    }

    override val primaryKey = PrimaryKey(id)
}