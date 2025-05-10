package org.example.project.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object AccountsDepartmentMessageTable: IdTable<Int>("department_message")  {
    override val id : Column<EntityID<Int>> = TaskMessageTable.integer("message_id").autoIncrement().entityId()

    val text = text("text")
    val createDate = text("create_date")
    val file = binary("file")
    val departmentId = integer("department_id")
    val userId = integer("user_id")

    init {
        AccountsDepartmentMessageTable.foreignKey(departmentId to AccountsDepartmentTable.id)
        AccountsDepartmentMessageTable.foreignKey( userId to UserTable.id)
    }
}