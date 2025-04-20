package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class GetPermissionResponse(
    val status: String, val description: String, val permissionList: List<PermissionAPI>
)
