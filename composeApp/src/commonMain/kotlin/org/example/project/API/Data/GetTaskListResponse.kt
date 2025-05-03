package org.example.project.API.Data

import org.example.project.Model.TaskWithID

data class GetTaskListResponse(
    val status: String,
    val description: String,
    val taskList: List<TaskWithID>
)
