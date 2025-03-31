package org.example.project.model

import kotlinx.serialization.Serializable
import org.example.project.API.UserAPI

@Serializable
data class CreateDepartmentResponse(val user : UserAPI, val accountsDepartment : AccountsDepartment)

