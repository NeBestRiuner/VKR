package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.TaskWithId

@Serializable
data class GetTaskListResponse(
    val status: String,
    val description: String,
    val taskList: List<TaskWithId>
)
