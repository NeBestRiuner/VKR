package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment
import org.example.project.model.TaskWithId

@Serializable

data class GetMessageListRequest(
    val accountsDepartment: AccountsDepartment,
    val taskWithID: TaskWithId
)
