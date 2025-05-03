package org.example.project.tables

import org.example.project.tables.EmployeePermissionTable.employeeId
import org.example.project.tables.EmployeePermissionTable.permissionId
import org.example.project.tables.TaskTable.autoIncrement
import org.example.project.tables.TaskTable.entityId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object EmployeeTaskTable: IdTable<Int>("employee_task") {
        override val id : Column<EntityID<Int>> = EmployeeTaskTable.integer("emp_task_id").autoIncrement().entityId()
        val taskId = integer("task_id")
        val employeeId = integer("employee_id")
        init {
                EmployeeTaskTable.foreignKey(employeeId to AccountsEmployeeTable.id)
                EmployeeTaskTable.foreignKey(taskId to TaskTable.id)
        }
}
