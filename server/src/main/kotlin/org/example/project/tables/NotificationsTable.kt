package org.example.project.tables

import org.example.project.tables.TaskMessageTable.taskId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object NotificationsTable: IdTable<Int>("notifications") {
    override val id : Column<EntityID<Int>> = NotificationsTable.integer("id").autoIncrement().entityId()
    val employeeId = integer("employee_id")
    val time = text("time")
    val content = text("content")
    val name = text("name")
    init{
        NotificationsTable.foreignKey(employeeId to AccountsEmployeeTable.id)
    }
}