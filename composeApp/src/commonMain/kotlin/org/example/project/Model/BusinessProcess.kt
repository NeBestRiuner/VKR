package org.example.project.Model

data class BusinessProcess(
    val id: Int,
    val name: String,
    val completed: Boolean,
    val bpTaskList: MutableList<BPTask>
)
