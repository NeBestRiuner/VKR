package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.Task

data class CreateTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val task: Task
)
