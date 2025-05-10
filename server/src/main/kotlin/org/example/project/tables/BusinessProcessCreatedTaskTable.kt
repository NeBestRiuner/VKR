package org.example.project.tables

import org.example.project.tables.BusinessProcessTable.autoIncrement
import org.example.project.tables.BusinessProcessTable.departmentId
import org.example.project.tables.BusinessProcessTable.entityId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object BusinessProcessCreatedTaskTable: IdTable<Int>("business_process_created_task")  {
    override val id : Column<EntityID<Int>> = BusinessProcessTable.integer("bp_ct_id").autoIncrement().entityId()
    val taskId = integer("task_id")
    val bpTaskId = integer("bp_task_id")
    init{
        BusinessProcessCreatedTaskTable.foreignKey( taskId to TaskTable.id)
        BusinessProcessCreatedTaskTable.foreignKey( bpTaskId to BusinessProcessTaskTable.id)
    }
}