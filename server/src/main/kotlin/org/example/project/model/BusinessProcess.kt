package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable

data class BusinessProcess(
    val id: Int,
    val name: String,
    val completed: Boolean,
    val bpTaskList: MutableList<BPTask>
)
