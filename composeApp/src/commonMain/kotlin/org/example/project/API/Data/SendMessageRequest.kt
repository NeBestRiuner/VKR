package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.Message
import org.example.project.Model.TaskWithID

data class SendMessageRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithID,
    val message: Message
)
