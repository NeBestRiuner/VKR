package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.TaskWithID

data class DeleteTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithID
)
