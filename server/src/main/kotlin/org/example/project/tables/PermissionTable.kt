package org.example.project.tables


import org.jetbrains.exposed.sql.Table

object PermissionTable: Table("permission") {
    val permissionId = integer("permission_id").autoIncrement()
    val permissionName = varchar("permission_name", 30)
    val description = varchar("description",200)

    override val primaryKey = PrimaryKey(permissionId)
}