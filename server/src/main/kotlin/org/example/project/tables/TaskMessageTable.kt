package org.example.project.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object TaskMessageTable: IdTable<Int>("task_message")  {
    override val id : Column<EntityID<Int>> = TaskMessageTable.integer("message_id").autoIncrement().entityId()

    val text = text("text")
    val createDate = text("create_date")
    val file = binary("file")
    val taskId = integer("task_id")
    val userId = integer("user_id")

    init {
        TaskMessageTable.foreignKey(taskId to TaskTable.id)
        TaskMessageTable.foreignKey( userId to UserTable.id)
    }

}