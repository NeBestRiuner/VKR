package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable
data class GetDepartmentListResponse(
    val status: String,
    val description: String,
    val departmentList: List<AccountsDepartmentAPI>
)
