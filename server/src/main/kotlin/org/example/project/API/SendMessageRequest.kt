package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment
import org.example.project.model.Message
import org.example.project.model.TaskWithId

@Serializable

data class SendMessageRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithId,
    val message: Message
)
