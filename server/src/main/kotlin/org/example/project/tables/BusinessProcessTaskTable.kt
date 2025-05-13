package org.example.project.tables

import org.example.project.tables.BusinessProcessTable.departmentId
import org.example.project.tables.TaskTable.autoIncrement
import org.example.project.tables.TaskTable.binary
import org.example.project.tables.TaskTable.bool
import org.example.project.tables.TaskTable.entityId
import org.example.project.tables.TaskTable.float
import org.example.project.tables.TaskTable.integer
import org.example.project.tables.TaskTable.text
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object BusinessProcessTaskTable: IdTable<Int>("business_process_task") {
    override val id : Column<EntityID<Int>> = BusinessProcessTaskTable.integer("bp_task_id").autoIncrement().entityId()
    val bpId = integer("bp_id")
    val position = integer("position")
    val name = text("name")
    val description = text("description")
    val duration = integer("duration")
    val priority = integer("priority")
    val percent = float("percent")
    val file = binary("file")
    val completed = bool("completed")
    val responsiblePost = text("responsible_post")
    init{
        BusinessProcessTaskTable.foreignKey( bpId to BusinessProcessTable.id)
    }
}