package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.TaskWithID


data class UpdateTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithID
)
