package org.example.project.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp

object TaskTable: IdTable<Int>("task") {
    override val id : Column<EntityID<Int>> = TaskTable.integer("task_id").autoIncrement().entityId()
    val name = text("name")
    val description = text("description")
    val creatorId = integer("creator_id")
    val beginTime = text("begin_time")
    val endTime = text("end_time")
    val priority = integer("priority")
    val percent = float("percent")
    val file = binary("file")
    val completed = bool("completed")
}