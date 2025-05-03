package org.example.project.API


import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment
import org.example.project.model.Task

@Serializable

data class CreateTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val task: Task
)
