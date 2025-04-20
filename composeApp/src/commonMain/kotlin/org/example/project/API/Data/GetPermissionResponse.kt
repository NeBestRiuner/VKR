package org.example.project.API.Data

import org.example.project.Model.Permission

data class GetPermissionResponse(
    val status: String, val description: String, val permissionList: List<Permission>
)
