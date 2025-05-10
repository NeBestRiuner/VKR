package org.example.project.tables

import org.example.project.tables.TaskMessageTable.taskId
import org.example.project.tables.TaskTable.autoIncrement
import org.example.project.tables.TaskTable.entityId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object BusinessProcessTable: IdTable<Int>("business_process") {
    override val id : Column<EntityID<Int>> = BusinessProcessTable.integer("bp_id").autoIncrement().entityId()
    val departmentId = integer("department_id")
    val name = text("name")
    val completed = bool("completed")
    init{
        BusinessProcessTable.foreignKey( departmentId to AccountsDepartmentTable.id)
    }
}