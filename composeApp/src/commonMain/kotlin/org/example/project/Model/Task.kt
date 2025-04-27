package org.example.project.Model

data class Task(
    val name: String,
    val description: String,
    val beginTime: String,
    val endTime: String,
    val priority: String,
    val percent: String,
    val file: ByteArray
)
