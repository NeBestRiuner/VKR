package org.example.project.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object HierarchyTable:IdTable<Int>("hierarchy") {
    override val id : Column<EntityID<Int>> = HierarchyTable.integer("id").autoIncrement().entityId()
    val postRectangles = binary("post_rectangles")
    val lines = binary("lines")
    val departmentId = integer("department_id")
    init {
        HierarchyTable.foreignKey(departmentId to AccountsDepartmentTable.id)
    }
}