package org.example.project.tables

import org.example.project.tables.HierarchyTable.autoIncrement
import org.example.project.tables.HierarchyTable.entityId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object TaskTable: IdTable<Int>("task") {
    override val id : Column<EntityID<Int>> = TaskTable.integer("task_id").autoIncrement().entityId()

}