package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.TaskWithID

data class GetMessageListRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithID
)
